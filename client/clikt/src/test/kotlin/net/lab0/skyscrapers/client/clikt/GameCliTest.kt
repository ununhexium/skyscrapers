package net.lab0.skyscrapers.client.clikt

import io.kotest.matchers.string.shouldNotContain
import net.lab0.skyscrapers.client.ServerIntegrationTest
import org.junit.jupiter.api.Test
import java.io.StringWriter

internal class GameCliTest: ServerIntegrationTest {
  @Test
  fun `connection integration test`() {

    useServer {
      val writer = StringWriter()

      val cli = GameCli.new(writer)
      cli.parse("config", "--reset")
      cli.parse("connect")

      writer.toString() shouldNotContain "Failed to connect to the server"
    }
  }
}
