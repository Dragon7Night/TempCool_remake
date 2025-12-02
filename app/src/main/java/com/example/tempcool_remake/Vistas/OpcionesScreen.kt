package com.example.tempcool_remake.Vistas

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

@Composable
fun Opciones(navController: NavController? = null) {

    // Colores
    val fondoApp = colorResource(id = R.color.bg_blue_deep)
    val btnColorGreen = colorResource(id = R.color.btn_green_pastel)
    val btnColorWhite = colorResource(id = R.color.white)
    val btnColorBlack = colorResource(id = R.color.black)

    // Imágenes
    val logoApp = painterResource(id = R.drawable.logo)

    // Contexto para Toast
    val context = LocalContext.current

    // Estado simulado del dispositivo (luego se reemplaza por Firebase / Arduino)
    var dispositivoConectado by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondoApp)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // -----------------------------
            //         TÍTULO PRINCIPAL
            // -----------------------------
            Text(
                text = "Panel del dispositivo",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = btnColorWhite,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // -----------------------------
            //      LOGO / ICONO CENTRAL
            // -----------------------------
            Image(
                painter = logoApp,
                contentDescription = "Logo App",
                modifier = Modifier.size(320.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // -----------------------------
            //     ESTADO DEL DISPOSITIVO
            // -----------------------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.white).copy(alpha = 0.12f))
                    .padding(16.dp)
            ) {
                Text(
                    "Estado del dispositivo:",
                    color = btnColorWhite,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    if (dispositivoConectado) "Conectado" else "Desconectado",
                    color = if (dispositivoConectado) btnColorGreen else colorResource(id = R.color.btn_cherry),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // -----------------------------
            //     BOTÓN ENCENDER VENTILADORES
            //     (listo para conectar lógica real)
            // -----------------------------
            Button(
                onClick = {
                    val estadoActual = dispositivoConectado
                    if (!estadoActual) {
                        Toast.makeText(context, "El dispositivo no está conectado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Enviando señal a ventiladores...", Toast.LENGTH_SHORT).show()
                        // Aquí luego irá la señal al Arduino/Firebase
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = btnColorGreen,
                    contentColor = btnColorBlack
                )
            ) {
                Text(
                    text = "Encender Ventiladores",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // -----------------------------
            //  MENSAJE INFORMATIVO AL USUARIO
            // -----------------------------
            Text(
                text = "Control del sistema de enfriamiento",
                color = btnColorWhite.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
