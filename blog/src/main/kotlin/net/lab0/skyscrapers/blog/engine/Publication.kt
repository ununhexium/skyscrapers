package net.lab0.skyscrapers.blog.engine

import java.time.Instant

/**
 * Marks this class a something that will be published
 */
interface Publication {
  /**
   * Unique ID of the publication.
   *
   * Must never be changed because it's use for permanent
   * referencing (permalink, cache, ...)
   */
  val id: String

  /**
   * The page will be made available after this time only.
   *
   * If null, always publish the page.
   */
  val after: Instant?

  /**
   * Indicates to the read when the page was read for the last time.
   */
  val lastUpdate: Instant

  /**
   * The content of the page.
   */
  val page: Page
}