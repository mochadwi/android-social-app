package io.mochadwi.data.datasource.webservice.local

import io.mochadwi.data.datasource.webservice.json.category.CategoryResponse
import io.mochadwi.data.datasource.webservice.json.category.MasterResponse

/**
 * Json reader
 */
interface JsonReader {
    fun getMasterCategory(name: String): List<MasterResponse>
    fun getCategoryByName(name: String): List<CategoryResponse>
}