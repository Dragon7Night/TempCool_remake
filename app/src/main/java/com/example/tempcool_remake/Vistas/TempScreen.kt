package com.example.tempcool_remake.Vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tempcool_remake.R
import com.google.firebase.database.*

@Composable
fun TempScreen(navController: NavController? = null) {
    // Colores
    val fondoApp = colorResource(id = R.color.bg_blue_deep)
    val textWhite = colorResource(id = R.color.white)

    val btnColorGreen = colorResource(id = R.color.btn_green_pastel)
    val btnColorDanger = colorResource(id = R.color.btn_cherry)

    var tempSensor1 by remember { mutableStateOf("Cargando...") }
    var tempSensor2 by remember { mutableStateOf("Cargando...") }
    var humidity by remember { mutableStateOf("Cargando...") }

    LaunchedEffect(Unit) {

        // se obtiene todos los datos del nodo de lectura, para su exposicion en tiempo real
        val databaseRef = FirebaseDatabase.getInstance().getReference("lecturas")


        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    // asignacion del las filas de cada temperatura
                    val tLm35 = snapshot.child("temp_lm35").getValue(Any::class.java)
                    val tDht = snapshot.child("temp_dht").getValue(Any::class.java)
                    val hum = snapshot.child("humedad").getValue(Any::class.java)

                    tempSensor1 = if (tLm35 != null) "$tLm35 °C" else "Sin datos"
                    tempSensor2 = if (tDht != null) "$tDht °C" else "Sin datos"
                    humidity = if (hum != null) "$hum %" else "Sin datos"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                tempSensor1 = "Error al leer datos"
                tempSensor2 = "Error al leer datos"
                humidity = "Error al leer datos"
            }
        })
    }

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

            SensorCard(
                title = "Temperatura - [LM35]",
                value = tempSensor1,
                color = textWhite
            )
            Spacer(modifier = Modifier.height(16.dp))

            SensorCard(
                title = "Temperatura - [DHT11]",
                value = tempSensor2,
                color = textWhite
            )
            Spacer(modifier = Modifier.height(16.dp))

            SensorCard(
                title = "Humedad del ambiente [DHT11]",
                value = humidity,
                color = textWhite
            )
        }
    }
}


// Funcion auxiliar filtradora de data de las temperaturas
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
