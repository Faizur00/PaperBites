package com.example.paperbites.ui.common

import androidx.compose.ui.graphics.Color
import com.hrm.latex.renderer.model.LatexConfig
import com.hrm.latex.renderer.model.LatexTheme
import org.junit.Assert.assertNotNull
import org.junit.Test

class LatexRendererTest {
    @Test
    fun testLatexConfig() {
        val config = LatexConfig()
        assertNotNull(config)
    }

    @Test
    fun testLatexThemeWithColor() {
        val themeLight = LatexTheme.light(color = Color.Black)
        val config = LatexConfig(theme = themeLight)
        assertNotNull(config)
    }
}

