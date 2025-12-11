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
import androidx.compose.ui.text.style.TextAlign
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
    // Recursos visuales
    val fondoApp = colorResource(id = R.color.bg_blue_deep)
    val textWhite = colorResource(id = R.color.white)

    // Lista mutable para guardar los datos que llegan de Firebase
    val listaTemperaturas = remember { mutableStateListOf<Temperatura>() }
    var cargando by remember { mutableStateOf(true) }

    // Conexión a Firebase (Nodo "historial")
    LaunchedEffect(Unit) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("historial")

        // Ordenamos por clave para ver los últimos registros (limitado a los últimos 50 para no saturar)
        databaseRef.limitToLast(50).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaTemperaturas.clear() // Limpiamos la lista antes de recargar

                // Recorremos cada hijo del nodo historial
                for (child in snapshot.children) {
                    val temp = child.getValue(Temperatura::class.java)
                    if (temp != null) {
                        temp.id = child.key // Guardamos el ID por si acaso
                        listaTemperaturas.add(temp)
                    }
                }
                // Invertimos la lista para que el más reciente salga arriba
                listaTemperaturas.reverse()
                cargando = false
            }

            override fun onCancelled(error: DatabaseError) {
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
                    // TABLA DINÁMICA (LazyColumn)
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
            containerColor = Color(0xFF2D3E50) // Un azul grisáceo para las tarjetas
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Fecha y Hora
            Text(
                text = "📅 ${item.timestamp}",
                color = Color(0xFFFFC107), // Color amarillo para resaltar fecha
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Fila de datos
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