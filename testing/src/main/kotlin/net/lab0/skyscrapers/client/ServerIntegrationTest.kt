package net.lab0.skyscrapers.client

import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.ServiceImpl
import net.lab0.skyscrapers.server.routed
import org.http4k.server.Undertow
import org.http4k.server.asServer
import kotlin.random.Random

interface ServerIntegrationTest {
  fun useServer(
    // TODO: could be next free port in some range instead of a random port that might be in use
    port: Int = Random.nextInt(10_000, 20_000),
    service: Service = ServiceImpl.new(),
    f: (url: String) -> Unit
  ) {
    val server = routed(service).asServer(Undertow(port)).start()
    val url = "http://localhost:$port/api/v1/"

    f(url)

    server.stop()
  }
}
