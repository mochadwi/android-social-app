package io.mochadwi.view.post.list

import android.os.Parcelable
import io.mochadwi.data.datasource.room.PostEntity
import io.mochadwi.domain.post.PostModel
import kotlinx.android.parcel.Parcelize

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 10/05/19
 * dedicated to build social-app
 *
 */

@Parcelize
data class PostItem(
        val userId: Int = 0, // 10
        val id: Int = 0, // 100
        val title: String = "", // at nam consequatur ea labore ea harum
        val body: String = "" // cupiditate quo est a modi nesciunt solutaipsa voluptas error itaque dicta inautem qui minus magnam et distinctio eumaccusamus ratione error aut
) : Parcelable {
    companion object {
        fun from(model: PostModel) = with(model) {
            PostItem(userId, id, title, body)
        }

        fun from(entity: PostEntity) = with(entity) {
            PostItem(userId, id, title, body)
        }
    }
}