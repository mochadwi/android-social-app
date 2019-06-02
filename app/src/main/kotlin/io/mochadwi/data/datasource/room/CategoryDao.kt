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

// This DAO is for forecast
@Dao
abstract class CategoryDao : BaseDao<CategoryEntity> {
    @Query("SELECT * FROM tbl_category WHERE type = :type")
    abstract fun getForecastByType(type: String): List<CategoryEntity>
}