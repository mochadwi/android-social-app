package io.mochadwi.util.base

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import io.mochadwi.R


/**
 * Created by mochadwi on 26/01/18.
 */

open class BaseActivity : AppCompatActivity() {

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
    }

    fun setupToolbar(toolbar: Toolbar?,
                     toolbarIcon: Int = R.drawable.ic_arrow_back_white_24dp,
                     isDetailActivity: Boolean = false,
                     newTitle: String = getString(R.string.app_name)) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(isDetailActivity)
            setDisplayUseLogoEnabled(!isDetailActivity)
            setHomeAsUpIndicator(toolbarIcon)
            title = newTitle
            subtitle = ""
        }
        toolbar?.title = newTitle
        toolbar?.subtitle = ""
    }
}