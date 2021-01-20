package com.example.pokmons.data.converters

import androidx.room.TypeConverter
import com.example.pokmons.data.serializables.Stats
import com.google.gson.Gson

class JSONConverter {

    @TypeConverter
    fun toJSON(list: List<Stats>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toList(json: String): List<Stats> {
        return Gson().fromJson(json, Array<Stats>::class.java).toList()
    }
}