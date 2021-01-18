package com.example.pokmons.data.converters

import androidx.room.TypeConverter
import com.example.pokmons.data.serializables.Result
import com.google.gson.Gson

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