package io.mochadwi.data.datasource.webservice

import io.mochadwi.data.datasource.webservice.json.category.CategoryResponse
import io.mochadwi.data.datasource.webservice.json.category.MasterResponse
import io.mochadwi.data.datasource.webservice.json.user.UsersResponse
import io.mochadwi.util.helper.AppHelper.Const.ENDPOINT_ALL
import io.mochadwi.util.helper.AppHelper.Const.ENDPOINT_MASTER
import io.mochadwi.util.helper.AppHelper.Const.ENDPOINT_MEN
import io.mochadwi.util.helper.AppHelper.Const.ENDPOINT_SEARCH_USERS
import io.mochadwi.util.helper.AppHelper.Const.ENDPOINT_WOMEN
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 10/05/19
 * dedicated to build social-app
 *
 */

interface AppWebDatasource {
    @GET(ENDPOINT_MASTER)
    fun getMasterCategoryAsync(): Deferred<List<MasterResponse>>

    @GET("{json}.json")
    fun getCategoryByNameAsync(
            @Path("json") name: String
    ): Deferred<List<CategoryResponse>>

    @GET(ENDPOINT_ALL)
    fun getAllCategoryAsync(): Deferred<List<CategoryResponse>>

    @GET(ENDPOINT_MEN)
    fun getMenCategoryAsync(): Deferred<List<CategoryResponse>>

    @GET(ENDPOINT_WOMEN)
    fun getWomenCategoryAsync(): Deferred<List<CategoryResponse>>

    @GET(ENDPOINT_SEARCH_USERS)
    fun getUsersAsync(
            @QueryMap param: Map<String, String>
    ): Deferred<UsersResponse>
}