package com.adyanf.githubsearchusers.ui.user

import com.adyanf.githubsearchusers.model.User

interface UsersContract {

    interface View {
        fun showUsers(users: List<User>, totalItems: Int)
        fun insertUsers(users: List<User>, startPosition: Int, itemCount: Int)
        fun hideUsers()
        fun clearUsers()
        fun showLoading()
        fun hideLoading()
        fun showEmptyResult(query: String)
        fun hideEmptyState()
        fun showError(message: String)
        fun showNetworkError()
    }

    interface Presenter {
        fun retrieveUsers(query: String, page: Int)
    }
}