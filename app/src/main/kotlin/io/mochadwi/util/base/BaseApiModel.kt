package io.mochadwi.util.base

import kotlinx.serialization.Serializable

@Serializable
data class BaseApiModel<T>(
        var message: String? = "",
        var documentation_url: String? = "",
        var items: T? = null
)