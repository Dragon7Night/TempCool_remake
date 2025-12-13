package com.example.tempcool_remake.Vistas

import android.content.Context
import android.util.Patterns
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
fun Register(navController: NavController? = null, auth: FirebaseAuth) {

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Colores
    val fondoApp = colorResource(id = R.color.bg_blue_deep)
    val btnColorCherry = colorResource(id = R.color.btn_cherry)
    val btnColorWhite = colorResource(id = R.color.white)

    // Logo arriba del formulario
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
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Registro de la cuenta",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo: nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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

            // Campo: correo
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo electrónico") },
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

            // Campo: contraseña
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

            // Campo: confirmar contraseña
            OutlinedTextField(
                value = confirmarContrasena,
                onValueChange = { confirmarContrasena = it },
                label = { Text("Confirmar contraseña") },
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

            Spacer(modifier = Modifier.height(24.dp))

            // Boton para crear la cuenta
            Button(
                onClick = {
                    validarRegistro(
                        nombre,
                        correo,
                        contrasena,
                        confirmarContrasena,
                        context,
                        auth
                    ) {
                        navController?.popBackStack()
                        navController?.navigate("login")
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
                Text( text = "Registrarse", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(7.dp))

            // Botón para ir directo al login si ya tiene cuenta
            TextButton(
                onClick = { navController?.navigate("login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "¿Tienes ya una cuenta creada?",
                    color = btnColorWhite
                )
            }
        }
    }
}

private fun validarRegistro(
    nombre: String,
    correo: String,
    contrasena: String,
    confirmarContrasena: String,
    context: Context,
    authFB: FirebaseAuth,
    onSuccess: () -> Unit
) {
    val correoLimpio = correo.trim()

    // Validaciones basicas para los campos
    if (nombre.isBlank() || correoLimpio.isBlank() || contrasena.isBlank() || confirmarContrasena.isBlank()) {
        Toast.makeText(context, "Ingrese todos los campos", Toast.LENGTH_SHORT).show()
        return
    }

    if (!Patterns.EMAIL_ADDRESS.matcher(correoLimpio).matches()) {
        Toast.makeText(context, "Correo inválido", Toast.LENGTH_SHORT).show()
        return
    }

    if (contrasena.length < 6) {
        Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
        return
    }

    if (contrasena != confirmarContrasena) {
        Toast.makeText(context, "No coinciden las contraseñas", Toast.LENGTH_SHORT).show()
        return
    }

    // Si esta todo OK se crea el usuario
    authFB.createUserWithEmailAndPassword(correoLimpio, contrasena).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
            onSuccess()
        } else {
            Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
        }
    }
}
