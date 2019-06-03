package io.mochadwi.view.post.list

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import io.mochadwi.databinding.PostItemBinding
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

class PostViewHolder(val binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root),
        BaseBindableAdapter<PostItem> {

    override fun bind(data: PostItem) {
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