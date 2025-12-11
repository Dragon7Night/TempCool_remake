package com.example.tempcool_remake.Vistas

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tempcool_remake.R



@Composable
fun Home(navController: NavController? = null) {
    // Variables de colores
    val fondoApp = colorResource(id = R.color.bg_blue_deep)
    val btnColorCherry = colorResource(id = R.color.btn_cherry)
    val btnColorWhite = colorResource(id = R.color.white)
    val btnColorBlack = colorResource(id = R.color.black)

    // Variables de imagenes
    val logoApp = painterResource(id = R.drawable.logo)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondoApp)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 42.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido a TemCool",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White // <--- CAMBIO A BLANCO
            )

            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = logoApp,
                contentDescription = "Logo App",
                modifier = Modifier.size(300.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Estado de la conexión: ACTIVO",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White // YA ERA BLANCO
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = { navController?.navigate("login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = btnColorWhite,
                    contentColor = btnColorBlack // Mantenido en negro para contraste con fondo blanco
                )
            ) {
                Text(text = "Iniciar Sesión")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController?.navigate("register") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = btnColorCherry,
                    contentColor = btnColorWhite // YA ERA BLANCO
                )
            ) {
                Text(text = "Registrarse")
            }
        }
    }
}