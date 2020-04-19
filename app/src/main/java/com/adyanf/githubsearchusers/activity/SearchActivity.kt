package com.adyanf.githubsearchusers.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adyanf.githubsearchusers.R
import com.adyanf.githubsearchusers.ui.user.UsersFragment

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, UsersFragment.newInstance())
                .commitNow()
        }
    }
}
