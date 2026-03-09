package com.ledsun.ainewstinder.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "feed_items",
    indices = [Index(value = ["guid"], unique = true)]
)
data class FeedItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val guid: String,
    val title: String,
    val link: String,
    val publishedAt: Instant,
    val fetchedAt: Instant,
    val isRead: Boolean = false,
    val isGood: Boolean = false,
    val isExcluded: Boolean = false
)
