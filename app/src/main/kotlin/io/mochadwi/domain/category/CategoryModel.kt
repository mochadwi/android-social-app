package io.mochadwi.domain.category

import android.os.Parcelable
import io.mochadwi.data.datasource.room.CategoryEntity
import io.mochadwi.data.datasource.webservice.json.category.CategoryResponse
import kotlinx.android.parcel.Parcelize

/**
 *
 * In syaa Allah created or modified by @mochadwi
 * On 13/05/19 for github-app
 */

@Parcelize
data class CategoryModel(
        val id: String = "", // mall50
        val name: String = "", // all50
        val status: String = "", // on_sale
        val num_likes: Int = 0, // 12
        val num_comments: Int = 0, // 49
        val price: Int = 0, // 77
        val photo: String = "" // https://dummyimage.com/400x400/000/fff?text=all50
) : Parcelable {
    companion object {
        fun from(response: CategoryResponse) = with(response) {
            CategoryModel(
                    id = id,
                    name = name,
                    status = status,
                    num_likes = num_likes,
                    num_comments = num_comments,
                    price = price,
                    photo = photo
            )
        }

        fun from(response: CategoryEntity) = with(response) {
            CategoryModel(
                    id = id,
                    name = name,
                    status = status,
                    num_likes = num_likes,
                    num_comments = num_comments,
                    price = price,
                    photo = photo
            )
        }
    }
}