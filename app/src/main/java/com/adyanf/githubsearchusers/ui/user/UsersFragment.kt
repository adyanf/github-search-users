package com.adyanf.githubsearchusers.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.adyanf.githubsearchusers.R
import com.adyanf.githubsearchusers.model.User
import com.adyanf.githubsearchusers.repository.remote.RemoteRepository
import com.adyanf.githubsearchusers.ui.customs.DividerItemDecoration
import com.adyanf.githubsearchusers.ui.listener.SimpleEndlessRecyclerScrollListener
import com.adyanf.githubsearchusers.util.debounce
import com.google.android.material.snackbar.Snackbar
import kotlin.coroutines.CoroutineContext
import kotlinx.android.synthetic.main.search_activity.*
import kotlinx.android.synthetic.main.users_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class UsersFragment : Fragment(), UsersContract.View, CoroutineScope {

    companion object {
        fun newInstance() = UsersFragment()

        private const val DELAY_DEBOUNCE = 1000L
    }

    override val coroutineContext: CoroutineContext
        get() = lifecycleScope.coroutineContext

    private val presenter = UsersPresenter(RemoteRepository(), this)
    private val adapter = UsersAdapter(mutableListOf())
    private val throttler = Channel<(() -> Unit)>(Channel.CONFLATED)

    private var query = ""
    private var totalItems = 0
    private var page = 1
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.users_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchTextListener()
        listenRenderThrottler()
        applyNewState()
    }

    private fun setupSearchTextListener() {
        activity?.let {
            val toolbar = it.toolbar
            val searchView = toolbar.findViewById<SearchView>(R.id.search)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean = false

                override fun onQueryTextChange(newText: String): Boolean {
                    hideEmptyState()
                    clearUsers()
                    applyNewState(newText)
                    if (newText.isNotBlank()) {
                        throttler.offer { presenter.retrieveUsers(newText, page) }
                    } else {
                        throttler.cancel()
                    }
                    return true
                }
            })
        }
    }

    private fun setupRecyclerView() {
        rvUserList.layoutManager = LinearLayoutManager(activity)
        rvUserList.adapter = adapter
        rvUserList.addOnScrollListener(
            SimpleEndlessRecyclerScrollListener(true) {
                loadMore()
            }
        )
        val dividerHeightInPixels = resources.getDimensionPixelSize(R.dimen.list_item_user_divider_height)
        val color = ContextCompat.getColor(context!!, R.color.colorDivider)
        rvUserList.addItemDecoration(DividerItemDecoration(color, dividerHeightInPixels))
    }

    private fun listenRenderThrottler() {
        launch(Dispatchers.Main) {
            throttler.debounce(DELAY_DEBOUNCE, this).consumeEach { it() }
        }
    }

    private fun loadMore() {
        val shouldLoadMore = !isLoading && (page + 1) * UsersPresenter.ITEM_PER_PAGE < totalItems
        if (shouldLoadMore) {
            page++
            presenter.retrieveUsers(query, page)
        }
    }

    private fun applyNewState(q: String = "") {
        this.query = q
        totalItems = 0
        page = 1
    }

    override fun showUsers(users: List<User>, totalItems: Int) {
        this.totalItems = totalItems
        rvUserList.visibility = View.VISIBLE
        adapter.updateUsers(users)
    }

    override fun insertUsers(users: List<User>, startPosition: Int, itemCount: Int) {
        rvUserList.visibility = View.VISIBLE
        adapter.insertUsers(users, startPosition, itemCount)
    }

    override fun hideUsers() {
        rvUserList.visibility = View.GONE
    }

    override fun clearUsers() {
        rvUserList.visibility = View.VISIBLE
        adapter.clearUsers()
    }

    override fun showLoading() {
        isLoading = true
        loadingIndicator.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        isLoading = false
        loadingIndicator.visibility = View.GONE
    }

    override fun showEmptyResult(query: String) {
        emptyState.text = resources.getString(R.string.empty_result, query)
        emptyState.visibility = View.VISIBLE
    }

    override fun showNetworkError() {
        emptyState.text = resources.getString(R.string.error_network)
        emptyState.visibility = View.VISIBLE
    }

    override fun hideEmptyState() {
        emptyState.visibility = View.GONE
    }

    override fun showError(message: String) {
        Snackbar.make(main, message, Snackbar.LENGTH_LONG).show()
    }
}
