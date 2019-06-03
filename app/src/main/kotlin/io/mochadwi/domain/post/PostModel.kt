package io.mochadwi.domain.post

import io.mochadwi.data.datasource.webservice.json.post.PostResponse

data class PostModel(
        val userId: Int = 0, // 10
        val id: Int = 0, // 100
        val title: String = "", // at nam consequatur ea labore ea harum
        val body: String = "" // cupiditate quo est a modi nesciunt solutaipsa voluptas error itaque dicta inautem qui minus magnam et distinctio eumaccusamus ratione error aut
) {
    companion object {
        fun from(post: PostResponse) = with(post) {
            PostModel(userId, id, title, body)
        }
    }
}