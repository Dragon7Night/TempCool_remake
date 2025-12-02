package com.example.tempcool_remake.Vistas

// Importaciones necesarias
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.* // Necesario para 'remember', 'mutableStateOf', 'LaunchedEffect'
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tempcool_remake.R
// Importaciones de Firebase Database
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// CAMBIO: El nombre de la función ahora es TempScreen para coincidir con tu archivo
@Composable
fun TempScreen(navController: NavController? = null) {
    val fondoApp = colorResource(id = R.color.bg_blue_deep)
    val textWhite = colorResource(id = R.color.white)

    var tempSensor1 by remember { mutableStateOf("Cargando...") }
    var tempSensor2 by remember { mutableStateOf("Cargando...") }
    var humidity by remember { mutableStateOf("Cargando...") }

    // Conexión a Firebase
    LaunchedEffect(Unit) {
        // CORRECCIÓN 1: Cambiar "Sensores" por "lecturas" (donde escribe el ESP32)
        val databaseRef = FirebaseDatabase.getInstance().getReference("lecturas")

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // CORRECCIÓN 2: Usar los nombres exactos que envía el ESP32
                    // CORRECCIÓN 3: Leer como Double/Float o Object, y convertir a String

                    // Lectura del LM35
                    val t1 = snapshot.child("temp_lm35").getValue(Any::class.java)
                    // Lectura del DHT11
                    val t2 = snapshot.child("temp_dht").getValue(Any::class.java)
                    // Lectura de Humedad
                    val hum = snapshot.child("humedad").getValue(Any::class.java)

                    // Convertimos a String de forma segura y agregamos unidades
                    tempSensor1 = if (t1 != null) "$t1 °C" else "Sin datos"
                    tempSensor2 = if (t2 != null) "$t2 °C" else "Sin datos"
                    humidity = if (hum != null) "$hum %" else "Sin datos"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                tempSensor1 = "Error"
            }
        })
    }

    // Interfaz de Usuario
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondoApp)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Temperaturas del dispositivo",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = textWhite,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(30.dp))

            // Tarjeta Sensor 1 (LM35)
            SensorCard(
                title = "Temperatura - [LM35]",
                value = tempSensor1,
                color = textWhite
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta Sensor 2
            SensorCard(
                title = "Temperatura - [DHT11]",
                value = tempSensor2,
                color = textWhite
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta Humedad
            SensorCard(
                title = "Humedad del ambiente [DHT11]",
                value = humidity,
                color = textWhite
            )
        }
    }
}

// Componente para el diseño de las tarjetas
@Composable
fun SensorCard(
    title: String,
    value: String,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x3344FFFFFF)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = color,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}