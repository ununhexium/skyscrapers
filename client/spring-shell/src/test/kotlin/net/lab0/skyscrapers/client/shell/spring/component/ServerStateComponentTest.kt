package net.lab0.skyscrapers.client.shell.spring.component

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.http4k.server.Undertow
import org.junit.jupiter.api.Test

internal class ServerStateComponentTest {
  @Test
  fun `start stop server`() {
    val port = 116

    val factory = mockk<Http4kServerFactoryComponent>() {
      every { new(port) } returns Undertow()
    }

    val comp  = ServerStateComponent(factory)
    comp.isRunning shouldBe false
    comp.startServer(port)
    comp.isRunning shouldBe true
    comp.stopServer()
    comp.isRunning shouldBe false
  }
}
