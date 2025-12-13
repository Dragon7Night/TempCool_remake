package com.example.tempcool_remake.Vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tempcool_remake.Model.Temperatura
import com.example.tempcool_remake.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun HistorialScreen(navController: NavController? = null) {
    // Colores que estoy usando en esta pantalla
    val fondoApp = colorResource(id = R.color.bg_blue_deep)
    val textWhite = colorResource(id = R.color.white)

    // Lista donde guardo lo que llega desde el nodo "historial"
    val listaTemperaturas = remember { mutableStateListOf<Temperatura>() }
    var cargando by remember { mutableStateOf(true) }

    // Me engancho a Firebase una sola vez cuando se crea la pantalla
    LaunchedEffect(Unit) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("historial")

        // Me quedo con los últimos 50 registros para no traer basura infinita
        databaseRef.limitToLast(50).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaTemperaturas.clear()

                // Recorro cada registro del historial y lo paso a mi data class
                for (child in snapshot.children) {
                    val temp = child.getValue(Temperatura::class.java)
                    if (temp != null) {
                        temp.id = child.key
                        listaTemperaturas.add(temp)
                    }
                }

                // Lo doy vuelta para que lo más nuevo quede arriba
                listaTemperaturas.reverse()
                cargando = false
            }

            override fun onCancelled(error: DatabaseError) {
                // Si Firebase falla, simplemente marco que ya no estoy cargando
                cargando = false
            }
        })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondoApp)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Historial de Registros",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = textWhite,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (cargando) {
                CircularProgressIndicator(color = textWhite)
            } else {
                if (listaTemperaturas.isEmpty()) {
                    Text("No hay registros disponibles", color = textWhite)
                } else {
                    // Lista con scroll para ir mostrando cada lectura guardada
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(listaTemperaturas) { item ->
                            HistorialItemCard(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistorialItemCard(item: Temperatura) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2D3E50) // Fondo de la tarjeta medio azulado
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Muestra la fecha/hora tal como viene desde Firebase
            Text(
                text = "📅 ${item.timestamp}",
                color = Color(0xFFFFC107),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Fila con las tres lecturas: LM35, DHT11 y humedad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DatosColumna(titulo = "LM35", valor = "${item.temp_lm35}°C")
                DatosColumna(titulo = "DHT11", valor = "${item.temp_dht}°C")
                DatosColumna(titulo = "Humedad", valor = "${item.humedad}%")
            }
        }
    }
}

@Composable
fun DatosColumna(titulo: String, valor: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = titulo, color = Color.Gray, fontSize = 12.sp)
        Text(text = valor, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}
