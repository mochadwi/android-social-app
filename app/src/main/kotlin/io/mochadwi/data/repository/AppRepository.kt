package io.mochadwi.data.repository

import io.mochadwi.data.datasource.room.*
import io.mochadwi.data.datasource.webservice.AppWebDatasource
import io.mochadwi.data.datasource.webservice.json.category.CategoryResponse
import io.mochadwi.data.datasource.webservice.json.category.MasterResponse
import io.mochadwi.domain.category.CategoryModel
import io.mochadwi.domain.category.MasterModel
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
    fun getMasterCategoryAsync(): Deferred<List<MasterModel>?>
    fun getCategoryByNameAsync(name: String?): Deferred<List<CategoryModel>?>
    fun getAllCategoryAsync(): Deferred<List<CategoryModel>?>
    fun getMenCategoryAsync(): Deferred<List<CategoryModel>?>
    fun getWomenCategoryAsync(): Deferred<List<CategoryModel>?>
    fun getUsersAsync(param: Map<String, String>?): Deferred<List<UserModel>?>
    fun getPostsAsync(): Deferred<List<PostModel>?>
}

/**
 * App repository
 * Make use of AppWebDatasource & add some cache
 */
class AppRepositoryImpl(
        private val appWebDatasource: AppWebDatasource,
        private val categoryDao: CategoryDao,
        private val userDao: UserDao,
        private val postDao: PostDao
) : AppRepository {

    override fun getMasterCategoryAsync(): Deferred<List<MasterModel>?> = coroutineAsync(IO) {
        remoteGetMasterCategoryAsync().await().map {
            MasterModel.from(it)
        }
    }

    private fun remoteGetMasterCategoryAsync(): Deferred<List<MasterResponse>> = coroutineAsync(IO) {
        val result = appWebDatasource.getMasterCategoryAsync().await()
        result
    }

    override fun getCategoryByNameAsync(name: String?): Deferred<List<CategoryModel>?> = coroutineAsync(IO) {
        remotegetCategoryByNameAsync(name ?: "all")
                .await()
                .map {
                    CategoryModel.from(it)
                }
    }

    private fun remotegetCategoryByNameAsync(name: String): Deferred<List<CategoryResponse>> = coroutineAsync(IO) {
        val result = appWebDatasource.getCategoryByNameAsync(name).await()
        result.map {
            categoryDao.upsert(CategoryEntity.from(it).copy(type = name))
        }
        result
    }

    override fun getAllCategoryAsync(): Deferred<List<CategoryModel>?> = coroutineAsync(IO) {
        val local = localGetAllCategoryAsync().await()
        val remote = remoteGetAllCategoryAsync().await()
        if (local.isNotEmpty()) local
        else remote.map { CategoryModel.from(it) }
    }

    private fun localGetAllCategoryAsync(): Deferred<List<CategoryModel>> = coroutineAsync(IO) {
        categoryDao.getForecastByType("all").map {
            CategoryModel.from(it)
        }
    }

    private fun remoteGetAllCategoryAsync(): Deferred<List<CategoryResponse>> = coroutineAsync(IO) {
        val result = appWebDatasource.getAllCategoryAsync().await()
        result.map {
            categoryDao.upsert(CategoryEntity.from(it).copy(type = "all"))
        }
        result
    }

    override fun getMenCategoryAsync(): Deferred<List<CategoryModel>?> = coroutineAsync(IO) {
        remoteGetMenCategoryAsync().await().map {
            CategoryModel.from(it)
        }
    }

    private fun remoteGetMenCategoryAsync(): Deferred<List<CategoryResponse>> = coroutineAsync(IO) {
        val result = appWebDatasource.getMenCategoryAsync().await()
        result
    }

    override fun getWomenCategoryAsync(): Deferred<List<CategoryModel>?> = coroutineAsync(IO) {
        remoteGetWomenCategoryAsync().await().map {
            CategoryModel.from(it)
        }
    }

    private fun remoteGetWomenCategoryAsync(): Deferred<List<CategoryResponse>> = coroutineAsync(IO) {
        val result = appWebDatasource.getWomenCategoryAsync().await()
        result
    }

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