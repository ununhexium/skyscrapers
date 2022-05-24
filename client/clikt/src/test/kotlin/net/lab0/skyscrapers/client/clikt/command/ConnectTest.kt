package net.lab0.skyscrapers.client.clikt.command

import io.kotest.matchers.string.shouldContain
import net.lab0.skyscrapers.client.ServerIntegrationTest
import net.lab0.skyscrapers.client.clikt.GameCli
import net.lab0.skyscrapers.client.clikt.parse
import org.junit.jupiter.api.Test
import java.io.StringWriter

internal class ConnectTest : ServerIntegrationTest {
  @Test
  fun `can connect to a server`() {
    useServer {  handler ->
      val output = StringWriter()

      val cli = GameCli.new(output, handler = handler)
      cli.parse("connect")

      output.toString() shouldContain "Connected."
    }
  }
}
