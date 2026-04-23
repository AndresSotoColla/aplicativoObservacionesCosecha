package com.example.aplicativoobservacionescosecha

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

object ExportManager {
    fun exportToExcel(context: Context) {
        val records = StorageManager.getRecords(context)
        if (records.isEmpty()) {
            Toast.makeText(context, "No hay registros para exportar", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Verificacion Cosecha")

            val headers = arrayOf(
                "ID", "Fecha", "Bloque", "Fruta Dejada", "Descarte Camas",
                "Plantas Sin Parir", "Fruta Joven", "Fruta No Aprovechable", "Fruta Adelantada",
                "Coronas", "Fruta Enferma", "Mortalidad", "Quema de Sol", "Daño Mecánico",
                "Ausente", "Bajo Peso", "Golpe de Agua", "Observaciones"
            )

            val headerRow = sheet.createRow(0)
            headers.forEachIndexed { index, header ->
                headerRow.createCell(index).setCellValue(header)
            }

            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

            records.forEachIndexed { rowIndex, record ->
                val row = sheet.createRow(rowIndex + 1)
                row.createCell(0).setCellValue(record.id)
                row.createCell(1).setCellValue(dateFormat.format(Date(record.timestamp)))
                row.createCell(2).setCellValue(record.bloque)
                row.createCell(3).setCellValue(record.frutaDejada.toDouble())
                row.createCell(4).setCellValue(record.descarteCamas.toDouble())
                row.createCell(5).setCellValue(record.plantasSinParir.toDouble())
                row.createCell(6).setCellValue(record.frutaJoven.toDouble())
                row.createCell(7).setCellValue(record.frutaNoAprovechable.toDouble())
                row.createCell(8).setCellValue(record.frutaAdelantada.toDouble())
                row.createCell(9).setCellValue(record.coronas.toDouble())
                row.createCell(10).setCellValue(record.frutaEnferma.toDouble())
                row.createCell(11).setCellValue(record.mortalidad.toDouble())
                row.createCell(12).setCellValue(record.quemaSol.toDouble())
                row.createCell(13).setCellValue(record.danoMecanico.toDouble())
                row.createCell(14).setCellValue(record.ausente.toDouble())
                row.createCell(15).setCellValue(record.bajoPeso.toDouble())
                row.createCell(16).setCellValue(record.golpeAgua.toDouble())
                row.createCell(17).setCellValue(record.observaciones)
            }

            val dateStr = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "Verificacion_Cosecha_$dateStr.xlsx"
            var outputStream: OutputStream? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    outputStream = resolver.openOutputStream(uri)
                }
            } else {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) downloadsDir.mkdirs()
                val file = File(downloadsDir, fileName)
                outputStream = FileOutputStream(file)
            }

            if (outputStream != null) {
                workbook.write(outputStream)
                workbook.close()
                outputStream.close()
                Toast.makeText(context, "Excel exportado a Descargas", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Error al crear archivo de destino", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error exportando Excel: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
