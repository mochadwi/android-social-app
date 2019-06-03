package io.mochadwi.data.datasource.webservice.local

import io.mochadwi.data.datasource.webservice.AppWebDatasource
import io.mochadwi.data.datasource.webservice.json.post.PostResponse
import io.mochadwi.data.datasource.webservice.json.user.UsersResponse
import kotlinx.coroutines.Deferred

/**
 * Read json files and render social date
 */
class LocalFileDataSource(val jsonReader: JsonReader, val delayed: Boolean) : AppWebDatasource {

    override fun getUsersAsync(param: Map<String, String>): Deferred<UsersResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPostsAsync(): Deferred<List<PostResponse>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        const val DEFAULT_CITY = "toulouse"
    }
}