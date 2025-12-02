package com.example.tempcool_remake.Vistas

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tempcool_remake.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PerfilUser(navController: NavController? = null) {

    // Firebase Authentication
    val user = FirebaseAuth.getInstance().currentUser

    // Datos del usuario
    val nombreUsuario = user?.displayName ?: "Perfil del usuario"
    val correoUsuario = user?.email ?: "Correo no disponible"

    // Colores del proyecto
    val fondoApp = colorResource(id = R.color.bg_blue_deep)
    val textWhite = colorResource(id = R.color.white)
    val btnColorCherry = colorResource(id = R.color.btn_cherry)
    val btnColorWhite = colorResource(id = R.color.white)

    // Imagen placeholder
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

            // ---------------------------
            //        FOTO DE PERFIL
            // ---------------------------
            Image(
                painter = tempApp,
                contentDescription = "Foto de perfil",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ---------------------------
            //        NOMBRE
            // ---------------------------
            Text(
                text = nombreUsuario,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = textWhite, // COLOR BLANCO
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            // ---------------------------
            //        CORREO
            // ---------------------------
            Text(
                text = correoUsuario,
                style = MaterialTheme.typography.titleMedium,
                color = textWhite, // COLOR BLANCO
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            // ---------------------------
            //    BOTÓN CERRAR SESIÓN
            // ---------------------------
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
                    contentColor = btnColorWhite // TEXTO DEL BOTÓN BLANCO
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