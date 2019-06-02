package io.mochadwi.data.datasource.webservice.param

import kotlinx.serialization.Serializable

/**
 *
 * In syaa Allah created or modified by @mochadwi
 * On 15/05/19 for github-app
 */

@Serializable
data class QueryParam(
        val q: String = "",
        val page: Int = 1,
        val per_page: Int = 15
)