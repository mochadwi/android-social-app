package io.mochadwi.data.datasource.webservice.local

import io.mochadwi.data.datasource.webservice.AppWebDatasource
import io.mochadwi.data.datasource.webservice.json.category.CategoryResponse
import io.mochadwi.data.datasource.webservice.json.category.MasterResponse
import io.mochadwi.data.datasource.webservice.json.user.UsersResponse
import io.mochadwi.util.ext.coroutineAsync
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay

/**
 * Read json files and render github date
 */
class LocalFileDataSource(val jsonReader: JsonReader, val delayed: Boolean) : AppWebDatasource {

    override fun getMasterCategoryAsync(): Deferred<List<MasterResponse>> = coroutineAsync(IO) {
        val result = jsonReader.getMasterCategory("master_response")
        if (delayed) delay(1_000)
        result
    }

    override fun getAllCategoryAsync(): Deferred<List<CategoryResponse>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMenCategoryAsync(): Deferred<List<CategoryResponse>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getWomenCategoryAsync(): Deferred<List<CategoryResponse>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCategoryByNameAsync(name: String): Deferred<List<CategoryResponse>> = coroutineAsync(IO) {
        val result = jsonReader.getCategoryByName("${name}_response")
        if (delayed) delay(1_000)
        result
    }

    override fun getUsersAsync(param: Map<String, String>): Deferred<UsersResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        const val DEFAULT_CITY = "toulouse"
    }
}