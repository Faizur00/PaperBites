package com.example.paperbites.ui.theme


import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.paperbites.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val libreBaskervilleFontFamily = FontFamily(
    Font(googleFont = GoogleFont("Libre Baskerville"), fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = GoogleFont("Libre Baskerville"), fontProvider = provider, weight = FontWeight.Bold),
)

val loraFontFamily = FontFamily(
    Font(googleFont = GoogleFont("Lora"), fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = GoogleFont("Lora"), fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = GoogleFont("Lora"), fontProvider = provider, weight = FontWeight.Bold),
)

val rationaleFontFamily = FontFamily(
    Font(googleFont = GoogleFont("Rationale"), fontProvider = provider, weight = FontWeight.Normal),
)

val jetbrainsMonoFontFamily = FontFamily(
    Font(googleFont = GoogleFont("JetBrains Mono"), fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = GoogleFont("JetBrains Mono"), fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = GoogleFont("JetBrains Mono"), fontProvider = provider, weight = FontWeight.Bold),
)
