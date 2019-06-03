package io.mochadwi.view.userdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.mochadwi.databinding.UserdetailFragmentBinding
import io.mochadwi.util.base.BaseUserActionListener
import io.mochadwi.util.ext.putArgs
import io.mochadwi.view.post.list.PostItem


/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 10/05/19
 * dedicated to build social-app
 *
 */
class UserDetailFragment : Fragment(), BaseUserActionListener {

    private lateinit var viewBinding: UserdetailFragmentBinding

    companion object {
        fun newInstance() = UserDetailFragment()
        fun newInstance(data: PostItem) = UserDetailFragment().putArgs {
            putParcelable("product_detail", data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        viewBinding = UserdetailFragmentBinding
                .inflate(inflater, container, false)
                .apply {
                    item = arguments?.getParcelable("product_detail")
                }

        return viewBinding.root
    }

    override fun onRefresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}