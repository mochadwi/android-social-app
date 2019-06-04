package io.mochadwi.view.post.list

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import io.mochadwi.databinding.PostItemBinding
import io.mochadwi.util.base.BaseBindableAdapter
import io.mochadwi.view.post.PostFragmentDirections

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
                val toPostDetail = PostFragmentDirections.toPostDetailAction(
                        data
                )

                it.findNavController().navigate(toPostDetail)
            }
            executePendingBindings()
        }
    }
}