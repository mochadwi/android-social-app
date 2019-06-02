package io.mochadwi.data.datasource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 10/05/19
 * dedicated to build github-app
 *
 */

@Database(entities = [CategoryEntity::class, UserEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun userDao(): UserDao
}