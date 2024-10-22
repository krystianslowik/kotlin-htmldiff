package com.jaraco.htmldiff

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HtmldiffApplication

fun main(args: Array<String>) {
	runApplication<HtmldiffApplication>(*args)
}