package io.mochadwi.view.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import io.mochadwi.databinding.PostdetailFragmentBinding


/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 10/05/19
 * dedicated to build social-app
 *
 */
class PostDetailFragment : Fragment() {

    private lateinit var viewBinding: PostdetailFragmentBinding
    private val args by navArgs<PostDetailFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        viewBinding = PostdetailFragmentBinding
                .inflate(inflater, container, false)
                .apply {
                    item = args.postItem
                }
        return viewBinding.root
    }
}