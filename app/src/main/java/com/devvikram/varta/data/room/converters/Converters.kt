package com.devvikram.varta.data.room.converters

import androidx.room.TypeConverter
import com.devvikram.varta.data.firebase.models.message.ForwardMetadata
import com.devvikram.varta.data.retrofit.models.Company
import com.devvikram.varta.data.retrofit.models.Project
import com.devvikram.varta.data.room.models.RoomForwardMetadata
import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date
import java.lang.reflect.Type

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>?): String = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }

    // Convert Map<String, String> to JSON string
    @TypeConverter
    fun fromStringMap(value: Map<String, String>?): String = gson.toJson(value)

    @TypeConverter
    fun toStringMap(value: String?): Map<String, String> {
        val type = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(value, type) ?: emptyMap()
    }

    // Convert Map<String, Long> to JSON string
    @TypeConverter
    fun fromLongMap(value: Map<String, Long>?): String = gson.toJson(value)

    @TypeConverter
    fun toLongMap(value: String?): Map<String, Long> {
        val type = object : TypeToken<Map<String, Long>>() {}.type
        return gson.fromJson(value, type) ?: emptyMap()
    }

    // Convert Map<String, Boolean> to JSON string
    @TypeConverter
    fun fromBooleanMap(value: Map<String, Boolean>?): String = gson.toJson(value)

    @TypeConverter
    fun toBooleanMap(value: String?): Map<String, Boolean> {
        val type = object : TypeToken<Map<String, Boolean>>() {}.type
        return gson.fromJson(value, type) ?: emptyMap()
    }

    // Convert ForwardMetadata to JSON string
    @TypeConverter
    fun fromForwardMetadata(value: ForwardMetadata?): String = gson.toJson(value)

    @TypeConverter
    fun toForwardMetadata(value: String?): ForwardMetadata? {
        val type = object : TypeToken<ForwardMetadata>() {}.type
        return gson.fromJson(value, type)
    }

    // Convert Firestore Timestamp to Long (epoch milliseconds)
    @TypeConverter
    fun fromTimestampToLong(timestamp: Timestamp?): Long? {
        return timestamp?.toDate()?.time
    }

    // Convert Long (epoch milliseconds) to Firestore Timestamp
    @TypeConverter
    fun fromLongToTimestamp(epochMilli: Long?): Timestamp? {
        return epochMilli?.let { Timestamp(Date(it)) }
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromRoomForwardMetadata(value: String?): RoomForwardMetadata? {
        val type: Type = object : TypeToken<RoomForwardMetadata?>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toRoomForwardMetadata(metadata: RoomForwardMetadata?): String? {
        return gson.toJson(metadata)
    }

    @TypeConverter
    fun fromCompanyListToString(companies: List<Company>?): String {
        return Gson().toJson(companies)
    }

    @TypeConverter
    fun fromStringToCompanyList(json: String?): List<Company> {
        return Gson().fromJson(json, object : TypeToken<List<Company>>() {}.type)
    }

    @TypeConverter
    fun fromProjectListToString(projects: List<Project>?): String {
        return Gson().toJson(projects)
    }

    @TypeConverter
    fun fromStringToProjectList(json: String?): List<Project> {
        return Gson().fromJson(json, object : TypeToken<List<Project>>() {}.type)
    }

}