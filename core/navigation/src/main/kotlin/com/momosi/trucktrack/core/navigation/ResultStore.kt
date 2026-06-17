package com.momosi.trucktrack.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

@Stable
class ResultStore {
    private val results = mutableMapOf<Any, Any?>()

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(key: Any): T? = results[key] as? T

    operator fun <T> set(key: Any, value: T) {
        results[key] = value
    }

    fun remove(key: Any): Any? = results.remove(key)

    companion object {
        val Saver = Saver<ResultStore, Map<Any, Any?>>(
            save = { it.results.toMap() },
            restore = { ResultStore().apply { results.putAll(it) } },
        )
    }
}

@Composable
fun rememberResultStore(): ResultStore = rememberSaveable(
    saver = ResultStore.Saver,
) {
    ResultStore()
}
