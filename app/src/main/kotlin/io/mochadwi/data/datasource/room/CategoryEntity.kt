package io.mochadwi.data.datasource.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.mochadwi.data.datasource.webservice.json.category.CategoryResponse
import io.mochadwi.domain.category.CategoryModel

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 10/05/19
 * dedicated to build github-app
 *
 */

// TODO: Use foreign key instead of type converter to handle 1:1, 1:M, M:1, M:M
// TODO: Handle list of object using typeconverter
@Entity(tableName = "tbl_category")
data class CategoryEntity(
        @PrimaryKey
        val id: String = "", // mall50
        val name: String = "", // all50
        val status: String = "", // on_sale
        val num_likes: Int = 0, // 12
        val num_comments: Int = 0, // 49
        val price: Int = 0, // 77
        val photo: String = "", // https://dummyimage.com/400x400/000/fff?text=all50
        val type: String = "" // women;men;all
) {
    companion object {
        fun from(model: CategoryResponse) = with(model) {
            CategoryEntity(
                    id = id,
                    name = name,
                    status = status,
                    num_likes = num_likes,
                    num_comments = num_comments,
                    price = price,
                    photo = photo
            )
        }

        fun from(model: CategoryModel) = with(model) {
            CategoryEntity(
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