package net.lab0.skyscrapers.client.http

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import net.lab0.skyscrapers.server.ServiceImpl
import net.lab0.skyscrapers.server.routed
import org.http4k.client.OkHttp
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class SkyscraperClientImplTest {
  private val randomPort = Random.nextInt(20_000, 30_000)

  @Test
  fun `can connect to a server`() {
    val server =
      routed(ServiceImpl.new()).asServer(Undertow(randomPort)).start()
    val client = SkyscraperClientImpl(OkHttp())

    client.connect("http://localhost:$randomPort/api/").shouldBeRight()

    // ssh port: nothing useful for us there
    client.connect("http://localhost:22/api/").shouldBeLeft()

    server.stop()
  }
}