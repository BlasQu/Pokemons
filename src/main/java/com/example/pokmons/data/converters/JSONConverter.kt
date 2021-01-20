package com.example.pokmons.data.converters

import androidx.room.TypeConverter
import com.example.pokmons.data.serializables.Stats
import com.google.gson.Gson

class JSONConverter {

    @TypeConverter
    fun toJSON(stats: List<Stats>): String {
        return Gson().toJson(stats)
    }

    @TypeConverter
    fun toStats(json: String): List<Stats> {
        return Gson().fromJson(json, Array<Stats>::class.java).toList()
    }
}