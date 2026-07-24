package com.example.paperbites.ui.common

import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TextFormattingTest {

    @Test
    fun parseFormattedText_plainText_returnsUnstyledString() {
        val input = "Simple title without tags"
        val result = parseFormattedText(input).annotatedString
        assertEquals("Simple title without tags", result.text)
        assertTrue(result.spanStyles.isEmpty())
    }

    @Test
    fun parseFormattedText_htmlEntities_decodesEntities() {
        val input = "Study of &lt;species&gt; &amp; &quot;sample&quot;"
        val result = parseFormattedText(input).annotatedString
        assertEquals("Study of <species> & \"sample\"", result.text)
    }

    @Test
    fun parseFormattedText_italicTags_appliesItalicStyle() {
        val input = "Study of <i>Escherichia coli</i> in vitro"
        val result = parseFormattedText(input).annotatedString
        assertEquals("Study of Escherichia coli in vitro", result.text)
        assertEquals(1, result.spanStyles.size)
        val styleSpan = result.spanStyles[0]
        assertEquals(9, styleSpan.start)
        assertEquals(25, styleSpan.end)
        assertEquals(FontStyle.Italic, styleSpan.item.fontStyle)
    }

    @Test
    fun parseFormattedText_boldTags_appliesBoldStyle() {
        val input = "Important: <b>Key result</b> here"
        val result = parseFormattedText(input).annotatedString
        assertEquals("Important: Key result here", result.text)
        assertEquals(1, result.spanStyles.size)
        val styleSpan = result.spanStyles[0]
        assertEquals(11, styleSpan.start)
        assertEquals(21, styleSpan.end)
        assertEquals(FontWeight.Bold, styleSpan.item.fontWeight)
    }

    @Test
    fun parseFormattedText_subscriptAndSuperscript_appliesBaselineShifts() {
        val input = "Chemical formula H<sub>2</sub>O and E=mc<sup>2</sup>"
        val result = parseFormattedText(input).annotatedString
        assertEquals("Chemical formula H2O and E=mc2", result.text)
        assertEquals(2, result.spanStyles.size)

        val subSpan = result.spanStyles.find { it.start == 18 && it.end == 19 }
        assertTrue(subSpan != null)
        assertEquals(BaselineShift.Subscript.multiplier, subSpan!!.item.baselineShift?.multiplier ?: 0f, 0.001f)

        val supSpan = result.spanStyles.find { it.start == 29 && it.end == 30 }
        assertTrue(supSpan != null)
        assertEquals(BaselineShift.Superscript.multiplier, supSpan!!.item.baselineShift?.multiplier ?: 0f, 0.001f)
    }

    @Test
    fun parseFormattedText_latexFormatting_appliesStyles() {
        val input = "Formula \\textit{species} and \\textbf{bold}"
        val result = parseFormattedText(input).annotatedString
        assertEquals("Formula species and bold", result.text)
        assertEquals(2, result.spanStyles.size)

        val italicSpan = result.spanStyles.find { it.start == 8 && it.end == 15 }
        assertTrue(italicSpan != null)
        assertEquals(FontStyle.Italic, italicSpan!!.item.fontStyle)

        val boldSpan = result.spanStyles.find { it.start == 20 && it.end == 24 }
        assertTrue(boldSpan != null)
        assertEquals(FontWeight.Bold, boldSpan!!.item.fontWeight)
    }

    @Test
    fun parseFormattedText_unclosedTag_handlesGracefully() {
        val input = "Unclosed <i>italic text at end"
        val result = parseFormattedText(input).annotatedString
        assertEquals("Unclosed italic text at end", result.text)
        assertEquals(1, result.spanStyles.size)
        assertEquals(9, result.spanStyles[0].start)
        assertEquals(27, result.spanStyles[0].end)
        assertEquals(FontStyle.Italic, result.spanStyles[0].item.fontStyle)
    }

    @Test
    fun parseFormattedText_latexDelimiters_extractsFormulas() {
        val input = "The equation \$E=mc^2\$ is famous, and so is \\( a^2 + b^2 = c^2 \\)."
        val result = parseFormattedText(input)
        
        // Check text has placeholders
        // "The equation " (13) + "[math]" (6) + " is famous, and so is " (22) + "[math]" (6) + "." (1) = 48
        assertEquals(48, result.annotatedString.length)
        
        // Check formulas are captured
        assertEquals(2, result.latexFormulas.size)
        assertEquals("E=mc^2", result.latexFormulas["latex_0"])
        assertEquals(" a^2 + b^2 = c^2 ", result.latexFormulas["latex_1"])
    }
}

