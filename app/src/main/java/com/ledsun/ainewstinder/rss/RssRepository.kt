package com.ledsun.ainewstinder.rss

import android.content.Context
import com.ledsun.ainewstinder.data.repository.FeedService
import com.ledsun.ainewstinder.db.AppDatabase

object RssRepository {
    fun getFeedService(context: Context): FeedService {
        val dao = AppDatabase.getInstance(context).feedItemDao()
        return FeedService(dao)
    }
}
