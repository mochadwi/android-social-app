package io.mochadwi.data.repository

import io.mochadwi.data.datasource.room.PostDao
import io.mochadwi.data.datasource.room.PostEntity
import io.mochadwi.data.datasource.room.UserDao
import io.mochadwi.data.datasource.room.UserEntity
import io.mochadwi.data.datasource.webservice.AppWebDatasource
import io.mochadwi.domain.post.PostModel
import io.mochadwi.domain.user.UserModel
import io.mochadwi.util.ext.coroutineAsync
import io.mochadwi.util.ext.default
import io.mochadwi.util.ext.sameContentWith
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 10/05/19
 * dedicated to build social-app
 *
 */

/**
 * App repository
 */
interface AppRepository {
    fun getUsersAsync(param: Map<String, String>?): Deferred<List<UserModel>?>
    fun getPostsAsync(): Deferred<List<PostModel>?>
}

/**
 * App repository
 * Make use of AppWebDatasource & add some cache
 */
class AppRepositoryImpl(
        private val appWebDatasource: AppWebDatasource,
        private val userDao: UserDao,
        private val postDao: PostDao
) : AppRepository {

    override fun getUsersAsync(param: Map<String, String>?): Deferred<List<UserModel>?> = coroutineAsync(IO) {
        if (param == null) localGetUsersAsync().await()
        else remoteGetUsersAsync(param).await()
    }

    private fun localGetUsersAsync(): Deferred<List<UserModel>> = coroutineAsync(IO) {
        userDao.getAllUsers().map {
            UserModel.from(it)
        }
    }

    private fun remoteGetUsersAsync(param: Map<String, String>): Deferred<List<UserModel>> = coroutineAsync(IO) {
        val result = appWebDatasource.getUsersAsync(param).await()

        // Check for api limitation
        if (result.incomplete_results) {
            result.items.map {
                userDao.upsert(UserEntity.from(it))
                UserModel.from(it)
            } + UserModel(id = -1) // flag the incomplete with -1
        } else {
            result.items.map {
                userDao.upsert(UserEntity.from(it))
                UserModel.from(it)
            }
        }
    }

    override fun getPostsAsync(): Deferred<List<PostModel>?> = coroutineAsync(IO) {
        val local = localGetPostsAsync().await() ?: emptyList()
        val remote = remoteGetPostsAsync().await() ?: emptyList()

        if ((local sameContentWith remote).default) local
        else remote
    }

    private fun localGetPostsAsync(): Deferred<List<PostModel>?> = coroutineAsync(IO) {
        postDao.getAllPosts().map {
            PostModel.from(it)
        }
    }

    private fun remoteGetPostsAsync(): Deferred<List<PostModel>?> = coroutineAsync(IO) {
        val result = appWebDatasource.getPostsAsync().await()
        result.map {
            postDao.upsert(PostEntity.from(it))
            PostModel.from(it)
        }
    }

}