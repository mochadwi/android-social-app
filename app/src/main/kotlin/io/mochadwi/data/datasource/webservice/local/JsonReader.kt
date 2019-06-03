package io.mochadwi.data.datasource.webservice.local

import io.mochadwi.data.datasource.webservice.json.post.PostResponse

/**
 * Json reader
 */
interface JsonReader {
    fun getPosts(name: String): List<PostResponse>
}