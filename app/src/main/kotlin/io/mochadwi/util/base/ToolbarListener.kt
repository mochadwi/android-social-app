package io.mochadwi.util.base

import androidx.appcompat.widget.Toolbar

/**
 *
 * In syaa Allah created or modified by @mochadwi
 * On 2019-06-04 for jsonplaceholder-app
 */

interface ToolbarListener {
    fun setupToolbar(toolbar: Toolbar)
    fun updateTitleToolbar(newTitle: String)
}