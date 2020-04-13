package com.adyanf.githubsearchusers.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Long,
    val login: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("html_url") val profileUrl: String,
    val type: String
)