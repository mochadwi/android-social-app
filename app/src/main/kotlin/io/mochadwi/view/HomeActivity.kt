package io.mochadwi.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import io.mochadwi.R
import io.mochadwi.databinding.HomeActivityBinding
import io.mochadwi.util.base.BaseActivity

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 10/05/19
 * dedicated to build github-app
 *
 */

class HomeActivity : BaseActivity() {
    private val viewBinding: HomeActivityBinding by lazy {
        DataBindingUtil.setContentView<HomeActivityBinding>(this, R.layout.home_activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_Launcher)
        super.onCreate(savedInstanceState)

        viewBinding.apply {
            setupToolbar(toolbar.tbCustom,
                    toolbarIcon = R.mipmap.ic_launcher)
        }
    }
}