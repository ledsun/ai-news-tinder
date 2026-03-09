package com.ledsun.ainewstinder.data.model

import java.time.Instant

data class RssItem(
    val guid: String,
    val title: String,
    val link: String,
    val publishedAt: Instant
)
