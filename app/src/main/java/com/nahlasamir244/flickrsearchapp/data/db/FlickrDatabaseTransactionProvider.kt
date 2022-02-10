package com.nahlasamir244.flickrsearchapp.data.db

import androidx.room.withTransaction
import javax.inject.Inject

class FlickrDatabaseTransactionProvider @Inject constructor(
    private val flickrSearchAppDatabase: FlickrSearchAppDatabase
) {
    suspend fun <R> runAsTransaction(block: suspend () -> R): R {
        return flickrSearchAppDatabase.withTransaction(block)
    }
}