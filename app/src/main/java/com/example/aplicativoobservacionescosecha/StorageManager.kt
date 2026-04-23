package com.example.aplicativoobservacionescosecha

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object StorageManager {
    private const val PREFS_NAME = "cosecha_prefs"
    private const val KEY_RECORDS = "records"
    private val gson = Gson()

    fun saveRecord(context: Context, record: CosechaRecord) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_RECORDS, "[]")
        val type = object : TypeToken<MutableList<CosechaRecord>>() {}.type
        val records: MutableList<CosechaRecord> = gson.fromJson(json, type) ?: mutableListOf()
        
        records.removeAll { it.id == record.id }
        records.add(record)

        prefs.edit().putString(KEY_RECORDS, gson.toJson(records)).apply()
    }

    fun getRecords(context: Context): List<CosechaRecord> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_RECORDS, "[]")
        val type = object : TypeToken<List<CosechaRecord>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun saveAllRecords(context: Context, records: List<CosechaRecord>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_RECORDS, gson.toJson(records)).apply()
    }
    
    fun deleteRecord(context: Context, id: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val records = getRecords(context).toMutableList()
        records.removeAll { it.id == id }
        prefs.edit().putString(KEY_RECORDS, gson.toJson(records)).apply()
    }
}
