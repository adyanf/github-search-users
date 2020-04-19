package com.adyanf.githubsearchusers.ui.user

import com.adyanf.githubsearchusers.model.User
import com.adyanf.githubsearchusers.repository.Repository
import com.adyanf.githubsearchusers.repository.remote.GithubSearchResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersPresenter(
    private val repository: Repository,
    private val view: UsersContract.View
) : UsersContract.Presenter {

    override fun retrieveUsers(query: String, page: Int) {
        view.showLoading()
        repository.retrieveUsers(query, page, object : Callback<GithubSearchResult<List<User>>> {
            override fun onFailure(call: Call<GithubSearchResult<List<User>>>, t: Throwable) {
                view.hideLoading()
                view.showNetworkError()
            }

            override fun onResponse(
                call: Call<GithubSearchResult<List<User>>>,
                response: Response<GithubSearchResult<List<User>>>
            ) {
                view.hideLoading()
                if (!response.isSuccessful) {
                    view.showError(response.message())
                    return
                }

                val data = response.body()
                data?.let { _data ->
                    val users = _data.items
                    val totalCount = _data.totalCount

                    if (users != null) {
                        if (page == 1) {
                            if (totalCount == 0) view.showEmptyResult(query)
                            view.showUsers(users, totalCount)
                        } else {
                            view.insertUsers(users, (page - 1) * ITEM_PER_PAGE, users.size)
                        }
                    }
                }
            }

        })
    }

    companion object {
        const val ITEM_PER_PAGE = 30
    }
}