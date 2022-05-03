package net.lab0.skyscrapers.blog.engine

import kotlinx.html.BODY
import kotlinx.html.body
import kotlinx.html.html
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
