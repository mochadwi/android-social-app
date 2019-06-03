package io.mochadwi.data.datasource.webservice.local

import io.mochadwi.data.datasource.webservice.json.post.PostResponse
import io.mochadwi.util.ext.fromJson
import kotlinx.serialization.list

/**
 * Common parts for Json reader
 */
abstract class BaseReader : JsonReader {

    private val json_file = ".json"

    override fun getPosts(name: String): List<PostResponse> =
            readJsonFile("$name$json_file").fromJson(PostResponse.serializer().list)

    abstract fun getAllFiles(): List<String>

    abstract fun readJsonFile(jsonFile: String): String
}