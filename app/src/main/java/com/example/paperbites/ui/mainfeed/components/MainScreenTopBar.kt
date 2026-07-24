package com.example.paperbites.ui.mainfeed.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.paperbites.ui.common.AnimatedIconButton
import kotlinx.coroutines.Job

@Composable
fun MainScreenTopBar(
    onOpenDrawer: () -> Unit
){
    Row(
        modifier = Modifier
            .statusBarsPadding()
            .height(68.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AnimatedIconButton(
            icon = Icons.Outlined.Menu,
            contentDescription = "menu",
            onClick = {
                onOpenDrawer()
            },
            iconColor = Color(0xFF1A1A1A),
            modifier = Modifier
        )

        AnimatedIconButton(
            icon = Icons.Outlined.Bookmarks,
            contentDescription = "bookmarks",
            onClick = {},
            iconColor = Color(0xFF1A1A1A),
            modifier = Modifier
        )
    }
}

