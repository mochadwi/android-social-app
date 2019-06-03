package io.mochadwi.data.datasource.webservice.json.post

import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
        val userId: Int = 0, // 10
        val id: Int = 0, // 100
        val title: String = "", // at nam consequatur ea labore ea harum
        val body: String = "" // cupiditate quo est a modi nesciunt solutaipsa voluptas error itaque dicta inautem qui minus magnam et distinctio eumaccusamus ratione error aut
)