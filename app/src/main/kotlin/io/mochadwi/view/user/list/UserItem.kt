package io.mochadwi.view.user.list

import android.os.Parcelable
import io.mochadwi.data.datasource.room.UserEntity
import io.mochadwi.domain.user.UserModel
import kotlinx.android.parcel.Parcelize

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 10/05/19
 * dedicated to build github-app
 *
 */

@Parcelize
data class UserItem(
        val id: Int = 0, // 1
        val login: String = "", // mojombo
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
) : Parcelable {
    companion object {
        fun from(model: UserModel) = with(model) {
            UserItem(id, login, node_id, avatar_url, gravatar_id, url, html_url, followers_url,
                    following_url, gists_url, starred_url, subscriptions_url, organizations_url,
                    repos_url, events_url, received_events_url, type, site_admin, score)
        }

        fun from(entity: UserEntity) = with(entity) {
            UserItem(id, login, node_id, avatar_url, gravatar_id, url, html_url, followers_url,
                    following_url, gists_url, starred_url, subscriptions_url, organizations_url,
                    repos_url, events_url, received_events_url, type, site_admin, score)
        }
    }
}