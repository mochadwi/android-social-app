package io.mochadwi.data.datasource.room

import androidx.room.*


/**
 * Created by mochadwi on 8/3/18.
 */
interface BaseDao<T> {
    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     * @return The SQLite row id
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(obj: T): Long

    /**
     * Insert an array of objects in the database.
     *
     * @param objList the objects to be inserted.
     * @return The SQLite row ids
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    fun insert(objList: List<T>): List<Long>

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    fun update(obj: T)

    /**
     * Update an array of objects from the database.
     *
     * @param objList the object to be updated
     */
    @Update
    fun update(objList: List<T>)

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    fun delete(obj: T)

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    @JvmSuppressWildcards
    fun delete(obj: List<T>)

    /**
     * Upsert for object
     *
     * @param obj
     */
    @Transaction
    fun upsert(obj: T) {
        val id = insert(obj)
        if (id == -1L) {
            update(obj)
        }
    }

    /**
     * TODO: Upsert for object list
     *
     * @param objList
     */


    /**
     * TODO: Delete old list and insert new list
     *
     * @param oldList
     */
}