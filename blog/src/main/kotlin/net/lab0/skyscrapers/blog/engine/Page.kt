package net.lab0.skyscrapers.blog.engine

/**
 * This will output an HTML page
 */
interface Page {
  val title: String

  val body: KotlinxHtmlRawBody

  /**
   * Build the HTML page output
   */
  fun build(): String
}