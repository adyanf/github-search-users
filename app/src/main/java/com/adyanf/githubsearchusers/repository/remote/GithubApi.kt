package com.adyanf.githubsearchusers.repository.remote

import com.adyanf.githubsearchusers.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {
    @GET("search/users")
    fun retrieveUsers(@Query("q") query: String, @Query("page") page: Int): Call<GithubSearchResult<List<User>>>
}