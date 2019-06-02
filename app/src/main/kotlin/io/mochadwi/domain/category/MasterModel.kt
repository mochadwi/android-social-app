package io.mochadwi.domain.category

import android.os.Parcelable
import io.mochadwi.data.datasource.webservice.json.category.MasterResponse
import kotlinx.android.parcel.Parcelize

/**
 *
 * In syaa Allah created or modified by @mochadwi
 * On 13/05/19 for social-app
 */

@Parcelize
data class MasterModel(
        val name: String = "", // Women
        val `data`: String = "" // https://api.social.com/Android/json/women.json
) : Parcelable {
    companion object {
        fun from(response: MasterResponse) = with(response) {
            MasterModel(
                    name = name,
                    `data` = `data`
            )
        }
    }
}