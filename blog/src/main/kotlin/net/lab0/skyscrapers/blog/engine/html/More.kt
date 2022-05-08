package net.lab0.skyscrapers.blog.engine.html

import kotlinx.html.BODY
import kotlinx.html.HTMLTag
import kotlinx.html.a
import kotlinx.html.attributesMapOf
import kotlinx.html.div
import kotlinx.html.img
import kotlinx.html.visit
import net.lab0.skyscrapers.blog.IMAGES_PATH_SEGMENT


@DslMarker
annotation class MyMarker

@MyMarker
fun HTMLTag.googleSearchImageLink(
  what: String,
  classes: String? = null
): Unit = BODY(
  attributesMapOf("class", classes), consumer
).visit {
  a(href = "https://www.google.com/search?tbm=isch&q=$what") {
    +what
  }
}


@MyMarker
fun HTMLTag.image(
  src: String,
  alt: String? = null,
  classes: String? = null
): Unit = BODY(
  attributesMapOf("class", classes), consumer
).visit {
  div(classes = ".imgbox") {
    img(alt, "$IMAGES_PATH_SEGMENT/$src", classes = "center-fit")
  }
}
