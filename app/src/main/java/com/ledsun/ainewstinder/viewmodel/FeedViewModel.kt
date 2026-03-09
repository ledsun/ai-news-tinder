package com.ledsun.ainewstinder.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ledsun.ainewstinder.db.AppDatabase
import com.ledsun.ainewstinder.data.model.FeedItem
import com.ledsun.ainewstinder.data.repository.FeedService
import com.ledsun.ainewstinder.data.repository.RSS_URL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).feedItemDao()
    private val feedService = FeedService(dao)

    val allItems: StateFlow<List<FeedItem>> = dao.getAllItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val goodItems: StateFlow<List<FeedItem>> = dao.getGoodItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val excludedItems: StateFlow<List<FeedItem>> = dao.getExcludedItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _refreshError = MutableStateFlow<String?>(null)
    val refreshError: StateFlow<String?> = _refreshError.asStateFlow()

    private val _feedUrl = MutableStateFlow(RSS_URL)
    val feedUrl: StateFlow<String> = _feedUrl.asStateFlow()

    private val _autoRefreshOnLaunch = MutableStateFlow(true)
    val autoRefreshOnLaunch: StateFlow<Boolean> = _autoRefreshOnLaunch.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
            _refreshError.value = null
            try {
                feedService.fetchAndSave()
            } catch (e: Exception) {
                _refreshError.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearRefreshError() {
        _refreshError.value = null
    }

    fun setRead(id: Long, isRead: Boolean) {
        viewModelScope.launch { dao.updateIsRead(id, isRead) }
    }

    fun setGood(id: Long, isGood: Boolean) {
        viewModelScope.launch { dao.updateIsGood(id, isGood) }
    }

    fun setExcluded(id: Long, isExcluded: Boolean) {
        viewModelScope.launch { dao.updateIsExcluded(id, isExcluded) }
    }

    fun copyExcludedUrlsAndClear(context: Context, onDone: () -> Unit) {
        viewModelScope.launch {
            val links = dao.getExcludedLinks()
            val text = links.joinToString("\n")
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("excluded_urls", text))
            dao.clearExcluded()
            onDone()
        }
    }

    fun clearExcluded() {
        viewModelScope.launch { dao.clearExcluded() }
    }

    fun setAutoRefreshOnLaunch(enabled: Boolean) {
        _autoRefreshOnLaunch.value = enabled
    }
}
