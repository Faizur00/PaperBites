package com.example.paperbites

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.paperbites.ui.PaperBitesNavHost
import com.example.paperbites.ui.mainfeed.MainFeedScreen
import com.example.paperbites.ui.theme.PaperBitesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                (context.applicationContext as PaperBitesApplication)
                    .container.paperRepository.seedIfNeeded()
            }

            PaperBitesTheme {
                PaperBitesNavHost()
            }
        }
    }
}


@Composable
fun PaperBytesPreviewWrapper(content: @Composable () -> Unit) {
    PaperBitesTheme {
        content()
    }
}

@Preview(device = Devices.PHONE, showBackground = true)
@Composable
fun MainPreviewPhone() {
    PaperBytesPreviewWrapper {
        MainFeedScreen(onOpenBookmarks = {})
    }
}

@Preview(device = Devices.TABLET, showBackground = true)
@Composable
fun MainPreviewTablet() {
    PaperBytesPreviewWrapper {
        MainFeedScreen(onOpenBookmarks = {})
    }
}
