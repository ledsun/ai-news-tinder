package com.ledsun.ainewstinder.db

import androidx.room.*
import com.ledsun.ainewstinder.data.model.FeedItem
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface FeedItemDao {

    @Query("SELECT * FROM feed_items ORDER BY publishedAt DESC")
    fun getAllItems(): Flow<List<FeedItem>>

    @Query("SELECT * FROM feed_items WHERE isGood = 1 ORDER BY publishedAt DESC")
    fun getGoodItems(): Flow<List<FeedItem>>

    @Query("SELECT * FROM feed_items WHERE isExcluded = 1 ORDER BY publishedAt DESC")
    fun getExcludedItems(): Flow<List<FeedItem>>

    @Query("SELECT link FROM feed_items WHERE isExcluded = 1 ORDER BY publishedAt DESC")
    suspend fun getExcludedLinks(): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<FeedItem>)

    @Query("UPDATE feed_items SET isRead = :isRead WHERE id = :id")
    suspend fun updateIsRead(id: Long, isRead: Boolean)

    @Query("UPDATE feed_items SET isGood = :isGood WHERE id = :id")
    suspend fun updateIsGood(id: Long, isGood: Boolean)

    @Query("UPDATE feed_items SET isExcluded = :isExcluded WHERE id = :id")
    suspend fun updateIsExcluded(id: Long, isExcluded: Boolean)

    @Query("UPDATE feed_items SET isExcluded = 0 WHERE isExcluded = 1")
    suspend fun clearExcluded()

    @Query("DELETE FROM feed_items WHERE publishedAt < :cutoff")
    suspend fun deleteOlderThan(cutoff: Instant)
}
