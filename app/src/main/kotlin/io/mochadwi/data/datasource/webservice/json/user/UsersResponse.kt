package io.mochadwi.data.datasource.webservice.json.user

import kotlinx.serialization.Serializable

// TODO: @mochadwi Add BaseResponse instead, and use items as generics data type
@Serializable
data class UsersResponse(
        val total_count: Int = 0, // 87732
        val incomplete_results: Boolean = false, // false
        val items: List<Item> = listOf()
) {
    @Serializable
    data class Item(
            val login: String = "", // mojombo
            val id: Int = 0, // 1
            val node_id: String = "", // MDQ6VXNlcjE=
            val avatar_url: String = "", // https://avatars0.githubusercontent.com/u/1?v=4
            val gravatar_id: String = "",
            val url: String = "", // https://api.github.com/users/mojombo
            val html_url: String = "", // https://github.com/mojombo
            val followers_url: String = "", // https://api.github.com/users/mojombo/followers
            val following_url: String = "", // https://api.github.com/users/mojombo/following{/other_user}
            val gists_url: String = "", // https://api.github.com/users/mojombo/gists{/gist_id}
            val starred_url: String = "", // https://api.github.com/users/mojombo/starred{/owner}{/repo}
            val subscriptions_url: String = "", // https://api.github.com/users/mojombo/subscriptions
            val organizations_url: String = "", // https://api.github.com/users/mojombo/orgs
            val repos_url: String = "", // https://api.github.com/users/mojombo/repos
            val events_url: String = "", // https://api.github.com/users/mojombo/events{/privacy}
            val received_events_url: String = "", // https://api.github.com/users/mojombo/received_events
            val type: String = "", // User
            val site_admin: Boolean = false, // false
            val score: Double = 0.0 // 97.246346
    )
}