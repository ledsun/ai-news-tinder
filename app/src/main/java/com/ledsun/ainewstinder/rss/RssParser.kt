package com.ledsun.ainewstinder.rss

import android.util.Xml
import com.ledsun.ainewstinder.data.model.RssItem
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

object RssParser {

    private val RSS_DATE_FORMATTER = DateTimeFormatter.ofPattern(
        "EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH
    )

    fun parse(inputStream: InputStream): List<RssItem> {
        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
        parser.setInput(inputStream, null)

        val items = mutableListOf<RssItem>()
        var inItem = false
        var currentTag = ""
        var guid = ""
        var title = ""
        var link = ""
        var pubDate = ""
        var dcDate = ""

        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            val localName = parser.name ?: ""

            when (eventType) {
                XmlPullParser.START_TAG -> {
                    currentTag = localName.lowercase()
                    if (currentTag == "item") {
                        inItem = true
                        guid = ""
                        title = ""
                        link = ""
                        pubDate = ""
                        dcDate = ""
                    }
                }
                XmlPullParser.END_TAG -> {
                    val endTag = localName.lowercase()
                    if (endTag == "item" && inItem) {
                        inItem = false
                        val effectiveGuid = guid.ifBlank { link }
                        if (effectiveGuid.isNotBlank() && title.isNotBlank()) {
                            val parsedDate = parseDate(pubDate.ifBlank { dcDate })
                            items.add(
                                RssItem(
                                    guid = effectiveGuid,
                                    title = title.trim(),
                                    link = link.trim(),
                                    publishedAt = parsedDate
                                )
                            )
                        }
                    }
                    currentTag = ""
                }
                XmlPullParser.TEXT -> {
                    if (inItem) {
                        val text = parser.text ?: ""
                        when (currentTag) {
                            "title" -> title += text
                            "link" -> link += text
                            "guid" -> guid += text
                            "pubdate" -> pubDate += text
                            "date" -> dcDate += text  // dc:date
                        }
                    }
                }
            }
            eventType = parser.next()
        }
        return items
    }

    private fun parseDate(dateStr: String): Instant {
        if (dateStr.isBlank()) return Instant.now()
        return try {
            ZonedDateTime.parse(dateStr.trim(), RSS_DATE_FORMATTER).toInstant()
        } catch (e: DateTimeParseException) {
            try {
                Instant.parse(dateStr.trim())
            } catch (e2: Exception) {
                Instant.now()
            }
        }
    }
}
