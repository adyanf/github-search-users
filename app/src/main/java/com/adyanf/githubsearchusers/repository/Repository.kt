package com.adyanf.githubsearchusers.repository

import com.adyanf.githubsearchusers.model.User
import com.adyanf.githubsearchusers.repository.remote.GithubSearchResult
import retrofit2.Callback

interface Repository {
    fun retrieveUsers(query: String, page: Int, callback: Callback<GithubSearchResult<List<User>>>)
}