package com.example.aplicativoobservacionescosecha

import java.util.UUID

data class CosechaRecord(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val bloque: String = "",
    val frutaDejada: Int = 0,
    val descarteCamas: Int = 0,
    val plantasSinParir: Int = 0,
    val frutaJoven: Int = 0,
    val frutaNoAprovechable: Int = 0,
    val frutaAdelantada: Int = 0,
    val coronas: Int = 0,
    val frutaEnferma: Int = 0,
    val mortalidad: Int = 0,
    val quemaSol: Int = 0,
    val danoMecanico: Int = 0,
    val ausente: Int = 0,
    val bajoPeso: Int = 0,
    val golpeAgua: Int = 0,
    val observaciones: String = ""
)
