package com.adyanf.githubsearchusers.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.whileSelect

@ExperimentalCoroutinesApi
fun <T> ReceiveChannel<T>.debounce(
    wait: Long = 300,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
): ReceiveChannel<T> = scope.produce {
    var value = receive()
    whileSelect {
        onTimeout(wait) {
            send(value)
            value = receive()
            true
        }
        onReceive {
            value = it
            true
        }
    }
}