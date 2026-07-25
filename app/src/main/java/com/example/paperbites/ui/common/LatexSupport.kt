package com.example.paperbites.ui.common

import androidx.collection.LruCache
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.hrm.latex.renderer.Latex
import com.hrm.latex.renderer.measure.LatexDimensions
import com.hrm.latex.renderer.measure.LatexMeasurerState
import com.hrm.latex.renderer.measure.rememberLatexMeasurer
import com.hrm.latex.renderer.model.LatexConfig
import com.hrm.latex.renderer.model.LatexTheme

/**
 * Helper class to hold parsed text and its associated inline LaTeX content.
 */
data class FormattedText(
    val annotatedString: AnnotatedString,
    val inlineContent: Map<String, InlineTextContent>
)

/**
 * Clean up unsupported or problematic LaTeX macros for fallback rendering.
 */
fun sanitizeFormula(formula: String): String {
    return formula
        .replace(Regex("""\\mathbf\{([^}]+)\}""")) { it.groupValues[1] }
        .replace(Regex("""\\text\{([^}]+)\}""")) { it.groupValues[1] }
        .replace(Regex("""\\mathrm\{([^}]+)\}""")) { it.groupValues[1] }
        .replace(Regex("""\\mathit\{([^}]+)\}""")) { it.groupValues[1] }
        .replace(Regex("""\\left"""), "")
        .replace(Regex("""\\right"""), "")
        .trim()
}

/**
 * Cache for LaTeX dimensions to avoid re-measuring frequently used or already seen formulas.
 */
object LatexCache {
    private val dimensionCache = LruCache<String, Pair<String, LatexDimensions>>(500)

    fun getDimensions(
        formula: String,
        config: LatexConfig,
        measurer: LatexMeasurerState,
        isDarkTheme: Boolean
    ): Pair<String, LatexDimensions>? {
        val key = "$formula-${config.fontSize.value}-$isDarkTheme"
        return dimensionCache.get(key) ?: run {
            // Try original formula first
            var targetFormula = formula
            var dimensions = try {
                measurer.measure(targetFormula, config, isDarkTheme)
            } catch (e: Exception) {
                null
            }

            // If original fails, try sanitized formula
            if (dimensions == null || dimensions.widthPx <= 0f || dimensions.heightPx <= 0f) {
                val sanitized = sanitizeFormula(formula)
                if (sanitized != formula) {
                    val sanitizedDimensions = try {
                        measurer.measure(sanitized, config, isDarkTheme)
                    } catch (e: Exception) {
                        null
                    }
                    if (sanitizedDimensions != null && sanitizedDimensions.widthPx > 0f && sanitizedDimensions.heightPx > 0f) {
                        targetFormula = sanitized
                        dimensions = sanitizedDimensions
                    }
                }
            }

            if (dimensions != null && dimensions.widthPx > 0f && dimensions.heightPx > 0f) {
                val result = Pair(targetFormula, dimensions)
                dimensionCache.put(key, result)
                result
            } else {
                null
            }
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
    fontSize: TextUnit,
    color: Color = Color.Unspecified,
    isDarkTheme: Boolean = isSystemInDarkTheme()
): FormattedText {
    val density = LocalDensity.current
    
    // Determine effective dark theme mode for formula rendering.
    // If a specific text color is provided, infer whether light or dark formula theme is needed.
    val effectiveIsDark = if (!color.isUnspecified) {
        val luminance = 0.299f * color.red + 0.587f * color.green + 0.114f * color.blue
        luminance > 0.5f // Light text color requires dark-theme LaTeX formula rendering
    } else {
        isDarkTheme
    }

    val measurer = rememberLatexMeasurer()

    return remember(rawText, fontSize, density, color, effectiveIsDark) {
        val parsed = parseFormattedText(rawText)
        val theme = if (!color.isUnspecified) {
            LatexTheme.light(color = color)
        } else if (effectiveIsDark) {
            LatexTheme.dark()
        } else {
            LatexTheme.light()
        }
        
        val config = LatexConfig(fontSize = fontSize, theme = theme)
        
        val inlineContent = parsed.latexFormulas.mapValues { (_, formula) ->
            val measuredResult = LatexCache.getDimensions(formula, config, measurer, effectiveIsDark)
            
            if (measuredResult != null) {
                val (renderFormula, dimensions) = measuredResult
                InlineTextContent(
                    Placeholder(
                        width = with(density) { dimensions.widthPx.toSp() },
                        height = with(density) { dimensions.heightPx.toSp() },
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                    )
                ) {
                    Latex(
                        latex = renderFormula,
                        modifier = Modifier.fillMaxSize(),
                        config = config,
                        isDarkTheme = effectiveIsDark
                    )
                }
            } else {
                // Fallback for malformed or unparseable LaTeX formula: display clean formula text with matching color
                val fallbackText = sanitizeFormula(formula).ifEmpty { formula }
                val estimatedCharWidth = fontSize.value * 0.55f
                val estimatedWidth = (estimatedCharWidth * fallbackText.length.coerceIn(1, 30)).sp

                InlineTextContent(
                    Placeholder(
                        width = estimatedWidth,
                        height = fontSize,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                    )
                ) {
                    Text(
                        text = fallbackText,
                        fontSize = fontSize,
                        color = if (!color.isUnspecified) color else (if (effectiveIsDark) Color.White else Color.Black)
                    )
                }
            }
        }
        
        FormattedText(parsed.annotatedString, inlineContent)
    }
}

