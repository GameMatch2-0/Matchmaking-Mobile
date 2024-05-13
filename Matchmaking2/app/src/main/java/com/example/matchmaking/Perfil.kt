package com.example.matchmaking

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.matchmaking.ui.theme.MatchmakingTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import com.example.matchmaking.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class Perfil : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = intent.extras

        setContent {
            MatchmakingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White

                ) {
                    TinderLikeCards()
                }
            }
        }
    }
}

@Composable
fun TinderLikeCards() {
    var usuarios by remember { mutableStateOf(mutableListOf<Usuario>()) }

    var showDialog by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        usuarios = fetchUsuarios().toMutableList()
    }

    val usuariosSnapshot = snapshotFlow { usuarios }.collectAsState(initial = mutableListOf())

    LazyRow(modifier = Modifier.fillMaxSize()) {
        items(usuariosSnapshot.value.size) { index ->
            val usuario = usuarios[index]
            var offsetX by remember { mutableStateOf(0f) }

            val offsetXState = animateFloatAsState(
                targetValue = offsetX,
                animationSpec = spring(), label = ""
            )

            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .width(width = 390.dp)
                    .height(height = 770.dp)
                    .padding(16.dp)
                    .graphicsLayer {
                        // var zIndex = usuarios.size - index.toInt()
                        translationX = offsetXState.value
                    }
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { delta ->
                            offsetX += delta
                        },
                        onDragStopped = {
                            if (offsetX > 100.dp.value || offsetX < 0.dp.value) {
                                usuarios.removeAt(index)
                            }
                            offsetX = 0f
                        }
                    )

            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.TopEnd)
                            .padding(16.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Button(
                            onClick = {
                                showText = true
                                coroutineScope.launch {
                                    delay(10000)
                                    showText = false
                                }
                            },
                            shape = CircleShape
                        ) {
                            Text("+")
                        }
                    }

                    Text(
                        text = usuario.nome,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }

    if (showDialog) {
        LaunchedEffect(Unit) {
            delay(1000)
            showDialog = false
        }

        Dialog(onDismissRequest = { showDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("❤️")
            }

        }

        if (showText) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Yellow),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Texto Exemplo",
                    modifier = Modifier.padding(16.dp),

                    )
            }
        }
    }
}

suspend fun fetchUsuarios(): List<Usuario> {
    val usuarios = mutableListOf<Usuario>()
    val querySnapshot = Firebase.firestore.collection("users").get().await()
    for (document in querySnapshot.documents) {
        document.toObject(Usuario::class.java)?.let { usuarios.add(it) }
    }
    return usuarios
}