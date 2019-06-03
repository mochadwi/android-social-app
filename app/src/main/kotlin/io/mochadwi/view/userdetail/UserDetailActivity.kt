package io.mochadwi.view.userdetail

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import io.mochadwi.R
import io.mochadwi.databinding.UserdetailActivityBinding
import io.mochadwi.util.base.BaseActivity
import io.mochadwi.util.ext.replaceFragmentInActivity
import io.mochadwi.view.post.list.PostItem

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 10/05/19
 * dedicated to build social-app
 *
 */

class UserDetailActivity : BaseActivity() {
    private val viewBinding: UserdetailActivityBinding by lazy {
        DataBindingUtil.setContentView<UserdetailActivityBinding>(this, R.layout.userdetail_activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val item = intent?.getParcelableExtra("product_detail") ?: PostItem()

        viewBinding.apply {
            setupToolbar(toolbar.tbCustom, isDetailActivity = true, newTitle = item.title)
            replaceFragmentInActivity(
                    UserDetailFragment.newInstance(item),
                    R.id.frameContainer)
        }
    }
}