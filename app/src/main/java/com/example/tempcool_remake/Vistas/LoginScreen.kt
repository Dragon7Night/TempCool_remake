package com.example.tempcool_remake.Vistas

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tempcool_remake.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Login(navController: NavController? = null, authFB: FirebaseAuth) {

    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    val context = LocalContext.current

    // Colores
    val fondoApp = colorResource(id = R.color.bg_blue_deep)
    val btnColorCherry = colorResource(id = R.color.btn_cherry)
    val btnColorWhite = colorResource(id = R.color.white)

    // Logo de la app
    val logoApp = painterResource(id = R.drawable.logo)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondoApp)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Boton para volver al home
        TextButton(
            onClick = { navController?.navigate("home") },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Text(
                text = "Home",
                fontWeight = FontWeight.Bold,
                color = btnColorWhite,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = logoApp,
                contentDescription = "Logo App",
                modifier = Modifier.size(300.dp)
            )
            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Inicio de sesión",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input de correo
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text(text = "Correo electrónico") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input de contraseña
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Boton de iniciar sesión
            Button(
                onClick = {
                    validarCredencial(correo, contrasena, authFB, context) {
                        if (it) navController?.navigate("options")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = btnColorCherry,
                    contentColor = btnColorWhite
                )
            ) {
                Text(text = "Iniciar sesión", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(7.dp))

            // Link para saltar al registro si no tiene cuenta
            TextButton(
                onClick = { navController?.navigate("register") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "¿Aún no tienes una cuenta? Regístrate",
                    color = btnColorWhite
                )
            }
        }
    }
}

private fun validarCredencial(
    correo: String,
    contrasena: String,
    authFB: FirebaseAuth,
    context: Context,
    onResult: (Boolean) -> Unit
) {
    if (correo.isNotEmpty() && contrasena.isNotEmpty()) {
        authFB.signInWithEmailAndPassword(correo, contrasena).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
                onResult(true)
            } else {
                Toast.makeText(context, "Error en el inicio de sesión", Toast.LENGTH_SHORT).show()
                onResult(false)
            }
        }
    } else {
        Toast.makeText(context, "Ingrese el correo y la contraseña", Toast.LENGTH_SHORT).show()
        onResult(false)
    }
}
