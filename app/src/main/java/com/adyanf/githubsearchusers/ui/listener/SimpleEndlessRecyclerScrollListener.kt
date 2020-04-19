package com.adyanf.githubsearchusers.ui.listener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SimpleEndlessRecyclerScrollListener(
    private val stopScrollPotentialEnergy: Boolean = false,
    private val onReachEnd: () -> Unit
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val manager = recyclerView.layoutManager as? LinearLayoutManager
        if (dy > 0 && manager != null) {
            val visibleItemCount = manager.childCount
            val totalItemCount = manager.itemCount
            val pastVisibleItems = manager.findFirstVisibleItemPosition()

            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                onReachEnd()
                if (stopScrollPotentialEnergy &&
                    manager.findLastCompletelyVisibleItemPosition() == totalItemCount - 1) {
                    recyclerView.stopScroll()
                }
            }
        }
    }
}