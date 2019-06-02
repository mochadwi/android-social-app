package io.mochadwi.util.mock

import io.mochadwi.data.datasource.room.UserEntity
import io.mochadwi.data.datasource.webservice.json.category.MasterResponse
import io.mochadwi.data.datasource.webservice.json.user.UsersResponse
import io.mochadwi.data.datasource.webservice.param.QueryParam
import io.mochadwi.domain.category.MasterModel
import io.mochadwi.domain.user.UserModel
import io.mochadwi.util.ext.fromJson
import io.mochadwi.util.ext.toQueryMap
import kotlinx.serialization.list

/**
 * Mock Github Data
 */
object MockedData {
    val mockMasterResponse = """
        [
  {
    "name": "All",
    "data": "https://api.github.com/Android/json/all.json"
  },
  {
    "name": "Men",
    "data": "https://api.github.com/Android/json/men.json"
  },
  {
    "name": "Women",
    "data": "https://api.github.com/Android/json/women.json"
  }
]
    """.fromJson(MasterResponse.serializer().list)

    val mockMasterModel = mockMasterResponse.map { MasterModel.from(it) }

    val mockUsersResponse = """
                    {
    "total_count": 1,
    "incomplete_results": false,
    "items": [
        {
            "login": "mochadwi",
            "id": 7057959,
            "node_id": "MDQ6VXNlcjcwNTc5NTk=",
            "avatar_url": "https://avatars3.githubusercontent.com/u/7057959?v=4",
            "gravatar_id": "",
            "url": "https://api.github.com/users/mochadwi",
            "html_url": "https://github.com/mochadwi",
            "followers_url": "https://api.github.com/users/mochadwi/followers",
            "following_url": "https://api.github.com/users/mochadwi/following{/other_user}",
            "gists_url": "https://api.github.com/users/mochadwi/gists{/gist_id}",
            "starred_url": "https://api.github.com/users/mochadwi/starred{/owner}{/repo}",
            "subscriptions_url": "https://api.github.com/users/mochadwi/subscriptions",
            "organizations_url": "https://api.github.com/users/mochadwi/orgs",
            "repos_url": "https://api.github.com/users/mochadwi/repos",
            "events_url": "https://api.github.com/users/mochadwi/events{/privacy}",
            "received_events_url": "https://api.github.com/users/mochadwi/received_events",
            "type": "User",
            "site_admin": false,
            "score": 119.12004
        }
    ]
}
                """.trimIndent().fromJson(UsersResponse.serializer())

    val mockUsersModel = mockUsersResponse.items.map {
        UserModel.from(it)
    }
    val mockUsersEntity = mockUsersResponse.items.map {
        UserEntity.from(it)
    }

    val mockUsername = "mochadwi"
    val mockUserQuery = QueryParam(q = mockUsername).toQueryMap()
}