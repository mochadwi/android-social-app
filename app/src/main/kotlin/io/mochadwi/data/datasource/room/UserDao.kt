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
abstract class UserDao : BaseDao<UserEntity> {
    @Query("SELECT * FROM tbl_user")
    abstract suspend fun getAllUsers(): List<UserEntity>
}