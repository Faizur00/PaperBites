package com.example.paperbites.ui.common

import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.em

/**
 * Result of parsing raw text with formatting tags and LaTeX formulas.
 */
data class ParsedFormattedText(
    val annotatedString: AnnotatedString,
    val latexFormulas: Map<String, String>
)

/**
 * Parses raw text containing HTML formatting tags (<i>, <b>, <sub>, <sup>, <em>, <strong>),
 * LaTeX formatting (\textit{}, \textbf{}, _{}, ^{}), and LaTeX math formulas ($...$, $$...$$, \(...\), \[...\])
 * into a styled Compose AnnotatedString with placeholders for LaTeX content.
 */
fun parseFormattedText(rawText: String): ParsedFormattedText {
    if (rawText.isEmpty()) return ParsedFormattedText(AnnotatedString(""), emptyMap())

    // Decode basic HTML entities first
    val decodedText = rawText
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&amp;", "&")
        .replace("&quot;", "\"")
        .replace("&apos;", "'")
        .replace("&#39;", "'")
        .replace("&nbsp;", " ")

    // Regex to match math formulas (display & inline), HTML tags, and LaTeX style tags
    val tagRegex = Regex(
        """\$\$(.*?)\$\$|\\\[(.*?)\\\]|\$([^\$]+)\$|\\\((.*?)\\\)|</?(i|em|b|strong|sub|sup|p|br|title)\b[^>]*>|\\(textit|textbf)\{|[_^]\{|\}""",
        setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
    )
    val latexFormulas = mutableMapOf<String, String>()

    val annotatedString = buildAnnotatedString {
        var currentIndex = 0

        data class StyleStackItem(val tagType: String, val startIndex: Int)
        val styleStack = mutableListOf<StyleStackItem>()

        tagRegex.findAll(decodedText).forEach { matchResult ->
            val tagMatch = matchResult.value
            val matchRange = matchResult.range

            // Append plain text before this tag
            if (matchRange.first > currentIndex) {
                append(decodedText.substring(currentIndex, matchRange.first))
            }
            currentIndex = matchRange.last + 1

            when {
                // Line breaks / paragraph breaks / title
                tagMatch.equals("<br>", ignoreCase = true) ||
                tagMatch.equals("<br/>", ignoreCase = true) ||
                tagMatch.equals("<br />", ignoreCase = true) -> {
                    append("\n")
                }
                tagMatch.equals("<p>", ignoreCase = true) -> {
                    if (length > 0 && !toAnnotatedString().text.endsWith("\n")) {
                        append("\n")
                    }
                }
                tagMatch.equals("</p>", ignoreCase = true) -> {
                    if (length > 0 && !toAnnotatedString().text.endsWith("\n")) {
                        append("\n")
                    }
                }
                tagMatch.equals("<title>", ignoreCase = true) -> {
                    styleStack.add(StyleStackItem("title", length))
                }
                tagMatch.equals("</title>", ignoreCase = true) -> {
                    val index = styleStack.indexOfLast { it.tagType == "title" }
                    if (index != -1) {
                        val item = styleStack.removeAt(index)
                        applyStyle(item.tagType, item.startIndex, length)
                    }
                }

                // Display math $$ ... $$
                tagMatch.startsWith("$$") -> {
                    val formula = matchResult.groupValues[1].trim()
                    if (formula.isNotEmpty()) {
                        val id = "latex_${latexFormulas.size}"
                        latexFormulas[id] = formula
                        appendInlineContent(id, "[math]")
                    }
                }
                // Display math \[ ... \]
                tagMatch.startsWith("\\[") -> {
                    val formula = matchResult.groupValues[2].trim()
                    if (formula.isNotEmpty()) {
                        val id = "latex_${latexFormulas.size}"
                        latexFormulas[id] = formula
                        appendInlineContent(id, "[math]")
                    }
                }
                // Inline math $ ... $
                tagMatch.startsWith("$") -> {
                    val formula = matchResult.groupValues[3].trim()
                    if (formula.isNotEmpty()) {
                        val id = "latex_${latexFormulas.size}"
                        latexFormulas[id] = formula
                        appendInlineContent(id, "[math]")
                    }
                }
                // Inline math \( ... \)
                tagMatch.startsWith("\\(") -> {
                    val formula = matchResult.groupValues[4].trim()
                    if (formula.isNotEmpty()) {
                        val id = "latex_${latexFormulas.size}"
                        latexFormulas[id] = formula
                        appendInlineContent(id, "[math]")
                    }
                }

                // LaTeX opening tags
                tagMatch.startsWith("\\textit{") -> {
                    styleStack.add(StyleStackItem("i", length))
                }
                tagMatch.startsWith("\\textbf{") -> {
                    styleStack.add(StyleStackItem("b", length))
                }
                tagMatch.startsWith("_{") -> {
                    styleStack.add(StyleStackItem("sub", length))
                }
                tagMatch.startsWith("^{") -> {
                    styleStack.add(StyleStackItem("sup", length))
                }

                // Closing brace for LaTeX tags
                tagMatch == "}" -> {
                    if (styleStack.isNotEmpty()) {
                        val last = styleStack.removeAt(styleStack.lastIndex)
                        applyStyle(last.tagType, last.startIndex, length)
                    }
                }

                // HTML closing tags
                tagMatch.startsWith("</") -> {
                    val tagName = matchResult.groupValues[5].lowercase()
                    val index = styleStack.indexOfLast { isMatchingTag(it.tagType, tagName) }
                    if (index != -1) {
                        val item = styleStack.removeAt(index)
                        applyStyle(item.tagType, item.startIndex, length)
                    }
                }

                // HTML opening tags
                tagMatch.startsWith("<") -> {
                    val tagName = matchResult.groupValues[5].lowercase()
                    if (tagName in listOf("i", "em", "b", "strong", "sub", "sup")) {
                        styleStack.add(StyleStackItem(tagName, length))
                    }
                }
            }
        }

        // Append any remaining trailing text
        if (currentIndex < decodedText.length) {
            append(decodedText.substring(currentIndex))
        }

        // Apply styles for any unclosed tags
        while (styleStack.isNotEmpty()) {
            val item = styleStack.removeAt(styleStack.lastIndex)
            applyStyle(item.tagType, item.startIndex, length)
        }
    }

    return ParsedFormattedText(annotatedString, latexFormulas)
}

private fun isMatchingTag(openTag: String, closeTag: String): Boolean {
    val open = openTag.lowercase()
    val close = closeTag.lowercase()
    return open == close ||
            (open in listOf("i", "em") && close in listOf("i", "em")) ||
            (open in listOf("b", "strong") && close in listOf("b", "strong"))
}

private fun AnnotatedString.Builder.applyStyle(tagType: String, start: Int, end: Int) {
    if (start >= end) return
    val style = when (tagType.lowercase()) {
        "i", "em" -> SpanStyle(fontStyle = FontStyle.Italic)
        "b", "strong" -> SpanStyle(fontWeight = FontWeight.Bold)
        "sub" -> SpanStyle(baselineShift = BaselineShift.Subscript, fontSize = 0.75.em)
        "sup" -> SpanStyle(baselineShift = BaselineShift.Superscript, fontSize = 0.75.em)
        "title" -> SpanStyle(fontWeight = FontWeight.Bold, fontSize = 1.25.em)
        else -> null
    }
    if (style != null) {
        addStyle(style, start, end)
    }
}
