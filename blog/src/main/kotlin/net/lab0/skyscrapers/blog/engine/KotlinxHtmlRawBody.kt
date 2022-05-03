package net.lab0.skyscrapers.blog.engine

import kotlinx.html.*
import kotlinx.html.stream.appendHTML

class KotlinxHtmlRawBody(private val raw: BODY.() -> Unit) : BodyLike {
  override fun asHtml() =
    StringBuffer()
      .also {
        it.appendHTML().html {
          body(block = raw)
        }
      }
      .toString()
}
