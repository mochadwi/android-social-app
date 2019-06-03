package io.mochadwi.data.datasource.room

import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = PostEntity::class)
@Entity(tableName = "tbl_post_fts")
data class PostFts(
        val title: String = "", // at nam consequatur ea labore ea harum
        val body: String = "" // cupiditate quo est a modi nesciunt solutaipsa voluptas error itaque dicta inautem qui minus magnam et distinctio eumaccusamus ratione error aut
)