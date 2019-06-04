package io.mochadwi.util.base

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import io.mochadwi.R


/**
 * Created by mochadwi on 26/01/18.
 */

open class BaseActivity : AppCompatActivity(), ToolbarListener {

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }

    override fun updateTitleToolbar(newTitle: String) {
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(true)
            title = newTitle
            subtitle = ""
        }
    }
}