package io.mochadwi.data.datasource.webservice.local

import io.mochadwi.data.datasource.webservice.json.category.CategoryResponse
import io.mochadwi.data.datasource.webservice.json.category.MasterResponse
import io.mochadwi.util.ext.fromJson
import kotlinx.serialization.list

/**
 * Common parts for Json reader
 */
abstract class BaseReader : JsonReader {

    private val json_file = ".json"

    override fun getMasterCategory(name: String): List<MasterResponse> =
            readJsonFile("$name$json_file").fromJson(MasterResponse.serializer().list)

    override fun getCategoryByName(name: String): List<CategoryResponse> =
            readJsonFile("$name$json_file").fromJson(CategoryResponse.serializer().list)

    abstract fun getAllFiles(): List<String>

    abstract fun readJsonFile(jsonFile: String): String
}