package io.mochadwi.data.datasource.webservice

import io.mochadwi.data.datasource.webservice.json.post.PostResponse
import io.mochadwi.data.datasource.webservice.json.user.UsersResponse
import io.mochadwi.util.helper.AppHelper.Const.ENDPOINT_POSTS
import io.mochadwi.util.helper.AppHelper.Const.ENDPOINT_SEARCH_USERS
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 10/05/19
 * dedicated to build social-app
 *
 */

interface AppWebDatasource {
    @GET(ENDPOINT_SEARCH_USERS)
    fun getUsersAsync(
            @QueryMap param: Map<String, String>
    ): Deferred<UsersResponse>

    @GET(ENDPOINT_POSTS)
    fun getPostsAsync(): Deferred<List<PostResponse>>
}