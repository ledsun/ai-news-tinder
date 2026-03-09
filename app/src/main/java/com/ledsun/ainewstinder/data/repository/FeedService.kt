package com.ledsun.ainewstinder.data.repository

import com.ledsun.ainewstinder.data.model.FeedItem
import com.ledsun.ainewstinder.data.model.RssItem
import com.ledsun.ainewstinder.db.FeedItemDao
import com.ledsun.ainewstinder.rss.RssParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.time.Instant
import java.time.temporal.ChronoUnit

const val RSS_URL = "https://ledsun.github.io/rss-fusion/merged.xml"

class FeedService(private val dao: FeedItemDao) {

    suspend fun fetchAndSave() {
        val rssItems = fetch()
        val now = Instant.now()
        val feedItems = rssItems.map { it.toFeedItem(now) }
        dao.insertAll(feedItems)
        dao.deleteOlderThan(now.minus(7, ChronoUnit.DAYS))
    }

    suspend fun deleteOldItems() {
        dao.deleteOlderThan(Instant.now().minus(7, ChronoUnit.DAYS))
    }

    private suspend fun fetch(): List<RssItem> = withContext(Dispatchers.IO) {
        val url = URL(RSS_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = 10_000
        connection.readTimeout = 15_000
        try {
            connection.connect()
            RssParser.parse(connection.inputStream)
        } finally {
            connection.disconnect()
        }
    }

    private fun RssItem.toFeedItem(fetchedAt: Instant) = FeedItem(
        guid = guid,
        title = title,
        link = link,
        publishedAt = publishedAt,
        fetchedAt = fetchedAt
    )
}
