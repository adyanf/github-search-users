package com.adyanf.githubsearchusers.repository.remote

import com.google.gson.annotations.SerializedName

class GithubSearchResult<T> {
    @SerializedName("total_count") var totalCount: Int = 0
    @SerializedName("incomplete_results") var inCompleteResults: Boolean = false
    var items: T? = null
}