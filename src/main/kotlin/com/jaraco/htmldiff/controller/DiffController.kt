package com.jaraco.htmldiff.controller

import com.github.difflib.DiffUtils
import com.github.difflib.patch.DeleteDelta
import com.github.difflib.patch.InsertDelta
import com.github.difflib.patch.ChangeDelta
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class DiffController {

    @GetMapping("/")
    fun index(): String {
        return "index"
    }

    @PostMapping("/compare")
    fun compare(
        @RequestParam("html1") html1: String,
        @RequestParam("html2") html2: String,
        model: Model
    ): String {
        return try {
            val originalDiff = generateHtmlDiffOriginal(html1, html2)
            val revisedDiff = generateHtmlDiffRevised(html1, html2)
            model.addAttribute("originalDiff", originalDiff)
            model.addAttribute("revisedDiff", revisedDiff)
            "result"
        } catch (e: Exception) {
            model.addAttribute("diffResult", "Error generating diff: ${e.message}")
            "result"
        }
    }

    @Throws(Exception::class)
    private fun generateHtmlDiffOriginal(originalHtml: String, revisedHtml: String): String {
        val original = prettyPrintHtml(originalHtml).lines()
        val revised = prettyPrintHtml(revisedHtml).lines()

        val patch = DiffUtils.diff(original, revised)

        val diffText = StringBuilder()
        var originalIndex = 0

        for (delta in patch.deltas) {
            val orig = delta.source

            // unchanged content before the delta
            while (originalIndex < orig.position) {
                diffText.append(escapeHtml(original[originalIndex])).append("\n")
                originalIndex++
            }

            when (delta) {
                is DeleteDelta, is ChangeDelta -> {
                    for (line in orig.lines) {
                        diffText.append("<del>").append(escapeHtml(line)).append("</del>\n")
                        originalIndex++
                    }
                }
                else -> {
                    for (line in orig.lines) {
                        diffText.append(escapeHtml(line)).append("\n")
                        originalIndex++
                    }
                }
            }
        }

        while (originalIndex < original.size) {
            diffText.append(escapeHtml(original[originalIndex])).append("\n")
            originalIndex++
        }

        return diffText.toString()
    }

    @Throws(Exception::class)
    private fun generateHtmlDiffRevised(originalHtml: String, revisedHtml: String): String {
        val original = prettyPrintHtml(originalHtml).lines()
        val revised = prettyPrintHtml(revisedHtml).lines()

        val patch = DiffUtils.diff(original, revised)

        val diffText = StringBuilder()
        var revisedIndex = 0

        for (delta in patch.deltas) {
            val rev = delta.target

            // unchanged content before the delta
            while (revisedIndex < rev.position) {
                diffText.append(escapeHtml(revised[revisedIndex])).append("\n")
                revisedIndex++
            }

            when (delta) {
                is InsertDelta, is ChangeDelta -> {
                    for (line in rev.lines) {
                        diffText.append("<ins>").append(escapeHtml(line)).append("</ins>\n")
                        revisedIndex++
                    }
                }
                else -> {
                    for (line in rev.lines) {
                        diffText.append(escapeHtml(line)).append("\n")
                        revisedIndex++
                    }
                }
            }
        }

        while (revisedIndex < revised.size) {
            diffText.append(escapeHtml(revised[revisedIndex])).append("\n")
            revisedIndex++
        }

        return diffText.toString()
    }

    // helper to escape HTML tags
    private fun escapeHtml(input: String): String {
        return input.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;")
    }

    // helper to pretty print
    private fun prettyPrintHtml(html: String): String {
        val document: Document = Jsoup.parse(html)
        document.outputSettings().prettyPrint(true)  // Enable pretty printing
        document.outputSettings().indentAmount(4)
        return document.outerHtml()
    }
}