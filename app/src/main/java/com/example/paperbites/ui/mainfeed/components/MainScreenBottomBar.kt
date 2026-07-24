package com.example.paperbites.ui.mainfeed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.paperbites.ui.common.SquaredButton
import com.example.paperbites.ui.theme.BGWhite

@Composable
fun MainScreenBottomBar(
    onClick: () -> Unit = {},
    whiteButtonIcon: ImageVector,
    whiteButtonText: String,
) {
    Row(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .height(86.dp)
            .background(BGWhite)
            .padding(start = 22.dp, end = 24.dp, bottom = 24.dp, top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SquaredButton(
            text = whiteButtonText,
            icon = whiteButtonIcon,
            onClick = onClick,
            bgColor = Color(0xFFFDFCFB),
            textColor = Color(0xFF1A1A1A),
            borderColor = Color(0xFF1A1A1A)
        )
        Spacer(Modifier.width(14.dp))
        SquaredButton(
            text = "READ FULL",
            onClick = { /* TODO */ },
            bgColor = Color(0xFF1A1A1A),
            textColor = Color(0xFFFDFCFB)
        )
    }
}

