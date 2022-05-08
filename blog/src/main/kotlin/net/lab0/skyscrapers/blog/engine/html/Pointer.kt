package net.lab0.skyscrapers.blog.engine.html

sealed class Pointer(val text: String) {
  abstract fun getHref(): String

  class Tag(text: String, val tag: String) : Pointer(text) {
    override fun getHref() =
      "https://github.com/ununhexium/skyscrapers/tree/$tag"
  }

  class Line(
    text: String,
    val tag: String,
    val sourceFolder: String,
    val file: String,
    val line: Int
  ) : Pointer(text) {
    override fun getHref() =
      "https://github.com/ununhexium/skyscrapers/blob/$tag/$sourceFolder/$file#L$line"
  }
}