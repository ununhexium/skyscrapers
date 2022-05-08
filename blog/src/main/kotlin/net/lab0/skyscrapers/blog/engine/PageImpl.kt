package net.lab0.skyscrapers.blog.engine

import kotlinx.html.body
import kotlinx.html.br
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.p
import kotlinx.html.script
import kotlinx.html.stream.appendHTML
import kotlinx.html.style
import kotlinx.html.title
import kotlinx.html.unsafe
import java.io.PrintWriter
import java.io.StringWriter

abstract class PageImpl : Page {

  override fun build() = try {
    StringBuffer().appendHTML().html {
      head {
        title { +this@PageImpl.title }

        style {
          +"""
            |.imgbox {
            |    display: grid;
            |    height: 100%;
            |}
            |.center-fit {
            |    max-width: 100%;
            |    max-height: 100vh;
            |    margin: auto;
            |}
          """.trimMargin()
        }
      }
      body {
        unsafe {
          +body.asHtml()
        }
      }
    }.toString()

  } catch (e: Exception) {
    StringWriter().use { stringWriter ->
      PrintWriter(stringWriter).use { problem ->
        e.printStackTrace(problem)
        val raw = stringWriter.buffer.toString()

        StringBuffer().appendHTML().html {
          body {
            p {
              raw.split("\n").forEach { line ->
                +line
                br()
              }
            }
          }
        }.toString()
      }
    }
  }
}
