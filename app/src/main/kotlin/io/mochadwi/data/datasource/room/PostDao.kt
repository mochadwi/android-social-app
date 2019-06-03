package io.mochadwi.data.datasource.room

import androidx.room.Dao
import androidx.room.Query

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 10/05/19
 * dedicated to build social-app
 *
 */

@Dao
abstract class PostDao : BaseDao<PostEntity> {
    @Query("SELECT * FROM tbl_post")
    abstract suspend fun getAllPosts(): List<PostEntity>

    @Query("SELECT * FROM tbl_post WHERE tbl_post MATCH :query")
    abstract suspend fun searchPosts(query: String): List<PostEntity>
}