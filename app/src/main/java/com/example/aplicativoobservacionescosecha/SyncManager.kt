package com.example.aplicativoobservacionescosecha

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SyncManager {
    private val client = OkHttpClient()
    private val gson = Gson()
    private val url = "https://interno.control.agricolaguapa.com/consultor/api/cargue_json_verificacion_cosecha"

    suspend fun syncData(context: Context, onResult: (String) -> Unit) {
        val records = StorageManager.getRecords(context).filter { !it.isSynced }
        
        if (records.isEmpty()) {
            withContext(Dispatchers.Main) {
                onResult("No hay datos nuevos para sincronizar.")
            }
            return
        }

        val dateFormatSystem = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val dateFormatMuestreo = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val payloadMapList = records.map {
            mapOf(
                "bloque" to it.bloque,
                "fecha" to dateFormatSystem.format(Date(it.timestamp)),
                "fecha_muestreo" to dateFormatMuestreo.format(Date(it.timestamp)),
                "usuario" to "tablet",
                "fruta_dejada_por_cosecha" to it.frutaDejada,
                "plantas_sin_parir" to it.plantasSinParir,
                "fruta_joven" to it.frutaJoven,
                "fruta_no_aprovechable" to it.frutaNoAprovechable,
                "fruta_adelantada" to it.frutaAdelantada,
                "coronas" to it.coronas,
                "fruta_enferma" to it.frutaEnferma,
                "daño_mecanico" to it.danoMecanico,
                "quema_de_sol" to it.quemaSol,
                "mortalidad" to it.mortalidad,
                "descarte_entre_camas" to it.descarteCamas,
                "golpe_de_agua" to it.golpeAgua,
                "bajo_peso" to it.bajoPeso,
                "ausente" to it.ausente,
                "observaciones" to it.observaciones
            )
        }

        val jsonPayload = gson.toJson(payloadMapList)
        val requestBody = jsonPayload.toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val bodyString = response.body?.string() ?: ""
                    // Consider it successful if the status code is 200
                    val allRecords = StorageManager.getRecords(context).toMutableList()
                    allRecords.forEach { diskRecord ->
                        if (records.any { r -> r.id == diskRecord.id }) {
                            diskRecord.isSynced = true
                        }
                    }
                    StorageManager.saveAllRecords(context, allRecords)
                    
                    withContext(Dispatchers.Main) {
                        onResult("Sincronización exitosa (${records.size} registros).")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onResult("Error de servidor: ${response.code}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onResult("Error de red: ${e.message}")
                }
            }
        }
    }
}
