package com.example.tempcool_remake.Vistas

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tempcool_remake.R
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.platform.LocalContext

@Composable
fun PerfilUser(navController: NavController? = null) {

    // Usuario actual de Firebase
    val user = FirebaseAuth.getInstance().currentUser

    val nombreUsuario = user?.displayName ?: "Perfil del usuario"
    val correoUsuario = user?.email ?: "Correo no disponible"

    // Colores de la pantalla
    val fondoApp = colorResource(id = R.color.bg_blue_deep)
    val textWhite = colorResource(id = R.color.white)
    val btnColorCherry = colorResource(id = R.color.btn_cherry)
    val btnColorWhite = colorResource(id = R.color.white)

    // Imagen temporal a modo de foto de perfil
    val tempApp = painterResource(id = R.drawable.temp)

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondoApp)
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            // imagen de perfil por ahora es una imagen fija
            Image(
                painter = tempApp,
                contentDescription = "Foto de perfil",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Nombre mostrado en grande
            Text(
                text = nombreUsuario,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = textWhite,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Correo debajo del nombre
            Text(
                text = correoUsuario,
                style = MaterialTheme.typography.titleMedium,
                color = textWhite,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botón para cerrar sesión
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                    navController?.navigate("home")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(bottom = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = btnColorCherry,
                    contentColor = btnColorWhite
                )
            ) {
                Text(
                    text = "Cerrar sesión",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
