package net.lab0.skyscrapers.blog.engine

import java.time.Instant

/**
 * Marks this class a something that will be published
 */
interface Publication {
  val after: Instant
  val lastUpdate: Instant
  val page: Page
}