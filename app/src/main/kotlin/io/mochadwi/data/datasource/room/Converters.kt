package io.mochadwi.data.datasource.room

import androidx.room.TypeConverter
import io.mochadwi.util.ext.fromJson
import io.mochadwi.util.ext.toJson
import kotlinx.serialization.internal.StringSerializer
import kotlinx.serialization.list
import java.util.*

/**
 * Help Convert Data types for Room
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromCategories(value: List<String>): String? {
        return value.toJson(StringSerializer.list)
    }

    @TypeConverter
    fun toCategories(value: String?): List<String>? {
        return value?.fromJson(StringSerializer.list)
    }
}