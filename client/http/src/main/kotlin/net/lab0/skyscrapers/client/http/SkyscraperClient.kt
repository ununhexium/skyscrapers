package net.lab0.skyscrapers.client.http

import arrow.core.Either
import org.http4k.core.Status

interface SkyscraperClient {
  /**
   * Choose which server to play on
   *
   * @param url The URL to the server, up to ".../api/"
   *
   * @return a menu client if successful is successful, `null` otherwise.
   */
  fun connect(url: String): Either<Status, SkyscraperMenuClient>
}
