package com.example.loginmatchmaking

import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.loginmatchmaking.ui.theme.LoginMatchMakingTheme
import javax.xml.validation.Validator
import kotlin.math.log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginMatchMakingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Tela("Android")
                }
            }
        }
    }
}

@Composable
fun Tela(name: String, modifier: Modifier = Modifier) {

    val valorEmail = remember { mutableStateOf("")}
    val valorSenha = remember { mutableStateOf("")}

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            Column {
                Text(text = "Faça seu login")
            }

            Spacer(modifier = Modifier.height(32.dp))

            fun isValidEmail(email: String): Boolean {
                val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
                return emailRegex.matches(email)
            }

            TextField(
                value = valorEmail.value,
                onValueChange = {valorEmail.value= it },
                label = {Text("Email")},
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                isError = !isValidEmail(valorEmail.value)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = valorSenha.value,
                onValueChange = {valorSenha.value= it},
                label = {Text("Senha")},
               // supportingText = {if (valDig == true)},
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            val context = LocalContext.current
              Button(onClick = {
                  if (valorSenha.value.isEmpty() || valorEmail.value.isEmpty()){
                      Toast.makeText(context ,"Valor inválido", Toast.LENGTH_SHORT).show()
                  }else if (valorSenha.value.length < 6) {
                      Toast.makeText(context ,"Deve conter mais de 6 caracteres", Toast.LENGTH_SHORT).show()
                  }
              }) {
               Text("Login")
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LoginMatchMakingTheme {
        Tela("Android")
    }
}