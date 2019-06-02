package io.mochadwi.data.datasource.webservice.json.category

import kotlinx.serialization.Serializable

@Serializable
data class MasterResponse(
        val name: String = "", // Women
        val `data`: String = "" // https://api.github.com/Android/json/women.json
)