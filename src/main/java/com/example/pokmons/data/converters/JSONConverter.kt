package com.example.pokmons.data.converters

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.pokmons.data.entities.Result
import com.google.gson.Gson
import kotlinx.serialization.json.Json

class JSONConverter {

    @TypeConverter
    fun toJSON(list: List<Result>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toList(json: String): List<Result> {
        return Gson().fromJson(json, Array<Result>::class.java).toList()
    }
}