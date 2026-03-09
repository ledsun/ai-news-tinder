package com.ledsun.ainewstinder.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ledsun.ainewstinder.data.model.FeedItem
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd HH:mm", Locale.getDefault())
    .withZone(ZoneId.systemDefault())

@Composable
fun FeedRow(
    item: FeedItem,
    onTap: () -> Unit,
    onToggleGood: () -> Unit,
    onToggleExcluded: () -> Unit,
    onMarkRead: () -> Unit,
    onHatena: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = when {
        item.isExcluded -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        item.isRead -> MaterialTheme.colorScheme.surface
        else -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor)
            .clickable {
                onMarkRead()
                onTap()
            }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = item.title,
            style = if (item.isRead) MaterialTheme.typography.bodyMedium
                    else MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = DATE_FORMATTER.format(item.publishedAt),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onToggleGood, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = if (item.isGood) Icons.Filled.Star else Icons.Filled.StarBorder,
                    contentDescription = "良かった",
                    tint = if (item.isGood) MaterialTheme.colorScheme.primary
                           else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(onClick = onToggleExcluded, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "除外候補",
                    tint = if (item.isExcluded) MaterialTheme.colorScheme.error
                           else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(onClick = onHatena, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Filled.Bookmark,
                    contentDescription = "はてブ",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
    HorizontalDivider()
}
