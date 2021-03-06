package io.mochadwi.view.post

import android.app.SearchManager
import android.content.ComponentName
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import io.mochadwi.R
import io.mochadwi.databinding.PostFragmentBinding
import io.mochadwi.domain.ErrorState
import io.mochadwi.domain.LoadingState
import io.mochadwi.domain.PostListState
import io.mochadwi.util.base.BaseApiModel
import io.mochadwi.util.base.BaseUserActionListener
import io.mochadwi.util.base.ToolbarListener
import io.mochadwi.util.ext.coroutineLaunch
import io.mochadwi.util.ext.default
import io.mochadwi.util.ext.fromJson
import io.mochadwi.util.list.EndlessRecyclerOnScrollListener
import io.mochadwi.view.HomeActivity
import io.mochadwi.view.post.list.PostItem
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.serialization.serializer
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.HttpException


/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 10/05/19
 * dedicated to build social-app
 *
 */
class PostFragment : Fragment(), BaseUserActionListener {

    private lateinit var viewBinding: PostFragmentBinding
    private val viewModel by viewModel<PostViewModel>()
    private lateinit var onLoadMore: EndlessRecyclerOnScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        viewBinding = PostFragmentBinding
                .inflate(inflater, container, false)
                .apply {
                    listener = this@PostFragment
                    vm = viewModel
                }

        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        if (::onLoadMore.isInitialized) onLoadMore.resetState()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (requireActivity() as ToolbarListener).updateTitleToolbar(newTitle = getString(R.string.app_name))
        setupObserver()
        if (savedInstanceState == null) {
            setupData()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(requireContext(), SearchManager::class.java)
        val componentName = ComponentName(requireContext(), HomeActivity::class.java)
        val searchItem = menu.findItem(R.id.actSearch)

        var searchFor = ""
        (searchItem?.actionView as SearchView).apply {
            // TODO: @mochadwi Definitely must using paging library, or upsert / delsert manually to the room
            setOnQueryTextListener(
                    object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            coroutineLaunch(Main) {
                                viewModel.keywords.send(query.default)
                            }
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            val searchText = newText.default.trim()

                            if (searchText == searchFor) return false

                            searchFor = searchText
                            coroutineLaunch(Main) {
                                delay(300)
                                if (searchText != searchFor)
                                    return@coroutineLaunch

                                viewModel.keywords.send(newText.default)
                            }
                            return true
                        }
                    }
            )
            setSearchableInfo(searchManager?.getSearchableInfo(componentName))
        }
    }

    override fun onRefresh() {
        Handler().postDelayed({
            pullToRefresh()
        }, 1000)
    }

    private fun setupData() {
        viewModel.apply {
            getPosts()
        }
    }

    private fun setupObserver() = with(viewModel) {
        // Observe ComposeState
        states.observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                when (state) {
                    is LoadingState -> showIsLoading()
                    is PostListState -> {
                        showCategoryItemList(
                                posts = state.list.map { PostItem.from(it) })
                    }
                    is ErrorState -> showError(state.error)
                }
            }
        })

        coroutineLaunch(Main) {
            keywords.consumeEach { searchPosts(it) }
        }
    }

    private fun pullToRefresh() {
        viewModel.apply {
            isRefreshing.set(true)
            if (::onLoadMore.isInitialized) onLoadMore.resetState()
            getPosts()
        }
    }

    private fun showIsLoading() = with(viewModel) {
        isError.set(false)
        progress.set(true)
        isRefreshing.set(false)
    }

    // TODO: Fix this bloated converter err handle
    private fun showError(err: Throwable) = with(viewBinding) {
        viewModel.apply {
            isError.set(true)
            progress.set(false)
            isRefreshing.set(false)

            val humanizedMsg = when (err) {
                is HttpException -> {
                    when (err.code()) {
                        403 -> "Try again later after a minute, you've exceeded the rate limit!"
                        422 -> "You have to type meaningful character, e.g: john doe"
                        else -> converter<Any>(err)?.message
                    }
                }
                else -> {
                    err.localizedMessage
                }
            }

            errMsg.set(humanizedMsg)

            error.btnRetry.setOnClickListener {
                if (::onLoadMore.isInitialized) onLoadMore.resetState()
                getPosts()
            }
        }
    }

    private fun showCategoryItemList(posts: List<PostItem>) {
        viewModel.apply {
            // TODO: @mochadwi clearing list doesn't good for pagination?
            postListSet.clear()
            postListSet.addAll(posts.toMutableList())
            isRefreshing.set(false)
            progress.set(false)
            isError.set(false)
        }
    }

    // TODO: Move this into utils class
    private inline fun <reified T : Any> converter(error: HttpException): BaseApiModel<T>? {
        var baseDao: BaseApiModel<T>? = null
        try {
            val body = error.response().errorBody()
            baseDao = body?.string()?.fromJson(BaseApiModel.serializer(T::class.serializer()))
        } catch (exception: Exception) {

        }

        return baseDao
    }
}