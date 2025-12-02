package com.example.tempcool_remake.Model

data class Temperatura(
    var id: String? = null,
    val temp_dht: Float = 0.0f,
    val humedad: Float = 0.0f,
    val temp_lm35: Float = 0.0f,
    val timestamp: String = ""
)