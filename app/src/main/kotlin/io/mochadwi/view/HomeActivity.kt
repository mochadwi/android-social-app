package io.mochadwi.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import io.mochadwi.R
import io.mochadwi.databinding.HomeActivityBinding
import io.mochadwi.util.base.BaseActivity

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 10/05/19
 * dedicated to build social-app
 *
 */

class HomeActivity : BaseActivity() {
    private val viewBinding: HomeActivityBinding by lazy {
        DataBindingUtil.setContentView<HomeActivityBinding>(this, R.layout.home_activity)
    }
    private lateinit var mNavHost: NavHostFragment
    private lateinit var mNavController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_Launcher)
        super.onCreate(savedInstanceState)

        viewBinding.executePendingBindings()
        setupNavController()
        setupAppBar()
        if (::mNavController.isInitialized && ::appBarConfiguration.isInitialized) {
            setupActionBar(mNavController, appBarConfiguration)
        }
    }

    override fun onBackPressed() {
        if (::mNavHost.isInitialized) {
            val fragmentsSize = mNavHost.childFragmentManager.fragments.size

            if (fragmentsSize >= 1) {
                super.onBackPressed()
            } else {
                findNavController(R.id.navHostFragment).navigateUp(appBarConfiguration)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (::mNavHost.isInitialized) {
            findNavController(R.id.navHostFragment).navigateUp(appBarConfiguration)
        } else {
            false
        }
    }

    private fun setupNavController() {
        mNavHost = supportFragmentManager
                .findFragmentById(R.id.navHostFragment) as NavHostFragment? ?: return
        mNavController = mNavHost.navController
    }

    private fun setupAppBar() {
        appBarConfiguration = AppBarConfiguration(
                setOf(R.id.postFragment),
                null
        )
    }

    private fun setupActionBar(
            navController: NavController,
            appBarConfig: AppBarConfiguration
    ) {
        setupToolbar(viewBinding.toolbar.tbCustom)
        setupActionBarWithNavController(navController, appBarConfig)
    }

}