package net.lab0.skyscrapers.client

import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.ServiceImpl
import net.lab0.skyscrapers.server.routed
import org.http4k.core.HttpHandler

/**
 * Connects to a fake server.
 */
interface FakeServerTest {
  fun fakeServer(
    service: Service = ServiceImpl.new(),
    f: (handler: HttpHandler) -> Unit
  ) {
    val server = routed(service)

    f(server)
  }
}
