package com.ledsun.ainewstinder.rss

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ledsun.ainewstinder.data.repository.FeedService
import com.ledsun.ainewstinder.db.AppDatabase

class FeedUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val dao = AppDatabase.getInstance(applicationContext).feedItemDao()
            val service = FeedService(dao)
            service.fetchAndSave()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
