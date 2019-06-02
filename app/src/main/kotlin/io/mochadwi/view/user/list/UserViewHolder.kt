package io.mochadwi.view.user.list

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import io.mochadwi.databinding.UserItemBinding
import io.mochadwi.util.base.BaseBindableAdapter
import io.mochadwi.util.ext.bundleOf
import io.mochadwi.util.ext.into
import io.mochadwi.util.ext.switchToPage
import io.mochadwi.view.userdetail.UserDetailActivity

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 10/05/19
 * dedicated to build social-app
 *
 */

class UserViewHolder(val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root),
        BaseBindableAdapter<UserItem> {

    override fun bind(data: UserItem) {
        binding.apply {
            item = data
            root.setOnClickListener {
                (root.context as FragmentActivity).switchToPage<UserDetailActivity>(
                        bundleOf("product_detail" into data)
                )
            }
            executePendingBindings()
        }
    }
}