package com.ledsun.ainewstinder.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.ledsun.ainewstinder.R
import com.ledsun.ainewstinder.ui.components.FeedRow
import com.ledsun.ainewstinder.viewmodel.FeedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoodScreen(viewModel: FeedViewModel) {
    val items by viewModel.goodItems.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.tab_good)) })
        }
    ) { paddingValues ->
        if (items.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.no_items))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            ) {
                items(items, key = { it.id }) { item ->
                    FeedRow(
                        item = item,
                        onTap = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
                            context.startActivity(intent)
                        },
                        onToggleGood = { viewModel.setGood(item.id, !item.isGood) },
                        onToggleExcluded = { viewModel.setExcluded(item.id, !item.isExcluded) },
                        onMarkRead = { viewModel.setRead(item.id, true) },
                        onHatena = {
                            val hatenaUrl = "https://b.hatena.ne.jp/entry/${Uri.encode(item.link)}"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(hatenaUrl))
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}
