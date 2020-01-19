package ch.hearc.minigolf.data.repositories

import ch.hearc.minigolf.data.stores.MinigolfStore

class MinigolfRepository private constructor(private val minigolfStore: MinigolfStore) {
    fun getMinigolfs() = minigolfStore.fetch()

    companion object {
        @Volatile private var instance: MinigolfRepository? = null
        fun getInstance(minigolfStore: MinigolfStore) = instance
            ?: synchronized(this) {
            instance
                ?: MinigolfRepository(minigolfStore).also { instance = it }
        }
    }
}