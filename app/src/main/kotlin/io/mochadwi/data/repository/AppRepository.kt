package io.mochadwi.data.repository

import io.mochadwi.data.datasource.room.PostDao
import io.mochadwi.data.datasource.room.PostEntity
import io.mochadwi.data.datasource.webservice.AppWebDatasource
import io.mochadwi.domain.post.PostModel
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
    fun getPostsAsync(): Deferred<List<PostModel>?>
    fun searchPostsAsync(query: String): Deferred<List<PostModel>?>
}

/**
 * App repository
 * Make use of AppWebDatasource & add some cache
 */
class AppRepositoryImpl(
        private val appWebDatasource: AppWebDatasource,
        private val postDao: PostDao
) : AppRepository {

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

    override fun searchPostsAsync(query: String): Deferred<List<PostModel>?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}