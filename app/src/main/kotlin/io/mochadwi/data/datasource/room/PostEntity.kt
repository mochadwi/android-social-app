package io.mochadwi.data.datasource.room

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import io.mochadwi.data.datasource.webservice.json.post.PostResponse

@Fts4
@Entity(tableName = "tbl_post")
data class PostEntity(
        @PrimaryKey
        val id: Int = 0, // 1
        val userId: Int = 0, // 10
        val title: String = "", // at nam consequatur ea labore ea harum
        val body: String = "" // cupiditate quo est a modi nesciunt solutaipsa voluptas error itaque dicta inautem qui minus magnam et distinctio eumaccusamus ratione error aut
) {
    companion object {
        fun from(response: PostResponse) = with(response) {
            PostEntity(id, userId, title, body)
        }
    }
}