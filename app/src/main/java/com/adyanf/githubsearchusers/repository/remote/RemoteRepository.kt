package com.adyanf.githubsearchusers.repository.remote

import com.adyanf.githubsearchusers.app.GithubSearchUsersApplication
import com.adyanf.githubsearchusers.model.User
import com.adyanf.githubsearchusers.repository.Repository
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteRepository : Repository {

    private val api: GithubApi

    init {
        val retrofit = Retrofit.Builder()
            .client(GithubSearchUsersApplication.INSTANCE.okHttpClient)
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create<GithubApi>(GithubApi::class.java)
    }

    override fun retrieveUsers(query: String, page: Int, callback: Callback<GithubSearchResult<List<User>>>) {
        api.retrieveUsers(query, page).enqueue(callback)
    }

    companion object {
        const val RATE_LIMIT_EXCEEDED = "rate limit exceeded"
    }
}