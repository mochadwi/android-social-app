package io.mochadwi.data.datasource.webservice.json.category

import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
        val id: String = "", // mall50
        val name: String = "", // all50
        val status: String = "", // on_sale
        val num_likes: Int = 0, // 12
        val num_comments: Int = 0, // 49
        val price: Int = 0, // 77
        val photo: String = "" // https://dummyimage.com/400x400/000/fff?text=all50
)