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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tempcool_remake.R
import com.google.firebase.database.*

@Composable
fun Opciones(navController: NavController? = null) {

    // Colores
    val fondoApp = colorResource(id = R.color.bg_blue_deep)
    val btnColorGreen = colorResource(id = R.color.btn_green_pastel)
    val btnColorWhite = colorResource(id = R.color.white)
    val btnColorBlack = colorResource(id = R.color.black)
    val btnColorDanger = colorResource(id = R.color.btn_cherry)

    // Logo del panel
    val logoApp = painterResource(id = R.drawable.logo)

    val context = LocalContext.current

    // Referencias a Firebase Realtime Database
    val db = remember { FirebaseDatabase.getInstance() }
    val cmdRef = remember { db.getReference("control/ventilador_cmd") }
    val lecturasRef = remember { db.getReference("lecturas") }

    // Estado del último comando que se haya mandado desde cualquier lado
    var ultimoCmd by remember { mutableStateOf<String?>(null) }

    // Última vez que se actualizó algo en "lecturas/ultima_actualizacion"
    var ultimaActualizacion by remember { mutableStateOf("Sin datos") }
    var ultimaLectura by remember { mutableStateOf(0L) }

    // Reloj interno para ir revisando si el ESP32 sigue mandando cosas
    var ahora by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(5_000)
            ahora = System.currentTimeMillis()
        }
    }

    // Si en los últimos 20 segundos no llega nada, se asume que el dispositivo está "offline"
    val dispositivoConectado = ultimaLectura != 0L &&
            (ahora - ultimaLectura) < 10_000L

    // Comandos de control
    LaunchedEffect(Unit) {
        // Escucha eventos
        cmdRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ultimoCmd = snapshot.getValue(String::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                // Si algo falla, se deja el último valor asignado
            }
        })

        // Escucha "lecturas/ultima_actualizacion"
        lecturasRef.child("ultima_actualizacion")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val ts = snapshot.getValue(String::class.java)
                    if (ts != null) {
                        ultimaActualizacion = ts
                        ultimaLectura = System.currentTimeMillis()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Si algo falla, se deja el último valor asignado
                }
            })
    }

    // Helper para mandar comandos simples tipo "VENT_ON"
    fun enviarComando(cmd: String) {
        cmdRef.setValue(cmd)
            .addOnSuccessListener {
                Toast.makeText(context, "Comando enviado: $cmd", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error al enviar comando: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Texto según el último comando visto en la RTDB
    val textoModoVentilador = when (ultimoCmd) {
        "VENT_ON" -> "Forzado ENCENDIDO"
        "VENT_OFF" -> "Forzado APAGADO"
        "VENT_AUTO" -> "Modo automático"
        null -> "Sin comando"
        else -> "Comando desconocido: $ultimoCmd"
    }

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

            // Título del panel
            Text(
                text = "Panel de configuraciones",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = btnColorWhite,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Logo
            Image(
                painter = logoApp,
                contentDescription = "Logo App",
                modifier = Modifier.size(270.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Carta de estados
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = btnColorWhite.copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    // Titulo del dispositivo
                    Text(
                        "Estado del dispositivo:",
                        color = btnColorWhite,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Estado del dispositivo
                    Text(
                        text = if (dispositivoConectado) "[ON LINE] Lecturas encontradas" else "[OFF LINE] Sin lecturas recientes",
                        color = if (dispositivoConectado) btnColorGreen else btnColorDanger,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Ultima conexion
                    Text(
                        text = "Última conexión: $ultimaActualizacion",
                        color = btnColorWhite,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Estado actual del ventilador
                    Text(
                        text = "Estado actual del ventilador: $textoModoVentilador",
                        color = btnColorWhite,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Zona de botones para controlar el ventilador
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Botón: modo automático
                Button(
                    onClick = { enviarComando("VENT_AUTO") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = btnColorWhite,
                        contentColor = btnColorBlack
                    )
                ) {
                    Text(
                        text = "Modo automático",
                        fontWeight = FontWeight.Bold, fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botón: forzar encendido
                Button(
                    onClick = { enviarComando("VENT_ON") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = btnColorGreen,
                        contentColor = btnColorBlack
                    )
                ) {
                    Text(
                        text = "Encender ventilador [MANUAL]",
                        fontWeight = FontWeight.Bold, fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botón de forzar apagado
                Button(
                    onClick = { enviarComando("VENT_OFF") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = btnColorDanger,
                        contentColor = btnColorWhite
                    )
                ) {
                    Text(
                        text = "Apagar ventilador [MANUAL]",
                        fontWeight = FontWeight.Bold, fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

        }
    }
}
