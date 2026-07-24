package com.example.paperbites.ui.common

import androidx.collection.LruCache
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import com.hrm.latex.renderer.Latex
import com.hrm.latex.renderer.measure.LatexDimensions
import com.hrm.latex.renderer.measure.LatexMeasurerState
import com.hrm.latex.renderer.measure.rememberLatexMeasurer
import com.hrm.latex.renderer.model.LatexConfig

/**
 * Helper class to hold parsed text and its associated inline LaTeX content.
 */
data class FormattedText(
    val annotatedString: AnnotatedString,
    val inlineContent: Map<String, InlineTextContent>
)

/**
 * Cache for LaTeX dimensions to avoid re-measuring frequently used or already seen formulas.
 */
object LatexCache {
    private val dimensionCache = LruCache<String, LatexDimensions>(500)

    fun getDimensions(
        formula: String,
        config: LatexConfig,
        measurer: LatexMeasurerState,
        isDarkTheme: Boolean
    ): LatexDimensions? {
        val key = "$formula-${config.fontSize.value}-${isDarkTheme}"
        return dimensionCache.get(key) ?: run {
            val dimensions = measurer.measure(formula, config, isDarkTheme)
            if (dimensions != null) {
                dimensionCache.put(key, dimensions)
            }
            dimensions
        }
    }
}

/**
 * Parses raw text and pre-measures any LaTeX formulas it contains, returning a [FormattedText]
 * object ready to be used with a Compose [androidx.compose.material3.Text] component.
 */
@Composable
fun rememberFormattedText(
    rawText: String,
    fontSize: TextUnit
): FormattedText {
    val density = LocalDensity.current
    val isDarkTheme = isSystemInDarkTheme()
    val measurer = rememberLatexMeasurer()
    
    return remember(rawText, fontSize, density, isDarkTheme) {
        val parsed = parseFormattedText(rawText)
        val config = LatexConfig(fontSize = fontSize)
        
        val inlineContent = parsed.latexFormulas.mapValues { (_, formula) ->
            val dimensions = LatexCache.getDimensions(formula, config, measurer, isDarkTheme)
            
            if (dimensions != null) {
                InlineTextContent(
                    Placeholder(
                        width = with(density) { dimensions.widthPx.toSp() },
                        height = with(density) { dimensions.heightPx.toSp() },
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                    )
                ) {
                    Latex(
                        latex = formula,
                        config = config,
                        isDarkTheme = isDarkTheme
                    )
                }
            } else {
                // Fallback for malformed LaTeX
                InlineTextContent(
                    Placeholder(
                        width = fontSize * 4, // Estimate for "[math]"
                        height = fontSize,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                    )
                ) {
                    androidx.compose.material3.Text(
                        text = "[math error]",
                        fontSize = fontSize,
                        color = androidx.compose.ui.graphics.Color.Red
                    )
                }
            }
        }
        
        FormattedText(parsed.annotatedString, inlineContent)
    }
}

