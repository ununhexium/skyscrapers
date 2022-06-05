package net.lab0.skyscrapers.client.clikt.command

import io.kotest.matchers.string.shouldContain
import net.lab0.skyscrapers.client.FakeServerTest
import net.lab0.skyscrapers.client.clikt.GameCliFactory
import net.lab0.skyscrapers.client.clikt.parse
import org.junit.jupiter.api.Test
import java.io.StringWriter

internal class ConnectTest : FakeServerTest {
  @Test
  fun `can connect to a server`() {
    fakeServer {  handler ->
      val output = StringWriter()

      val cli = GameCliFactory().new(output, handler = handler)
      cli.parse("connect")

      output.toString() shouldContain "Connected."
    }
  }
}
