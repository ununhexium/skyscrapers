package net.lab0.skyscrapers.client.clikt

import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import net.lab0.skyscrapers.client.ServerIntegrationTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.StringWriter

internal class GameCliTest : ServerIntegrationTest {
  @Test
  fun `connection integration test`() {

    useServer(port = 45678) {
      val writer = StringWriter()

      val cli = GameCli.new(writer, handler = it)
      cli.parse("config", "--reset")
      cli.parse("connect")

      writer.toString() shouldNotContain "Failed to connect to the server"
      writer.toString() shouldContain "Connected."
    }
  }

  @Test
  fun `create a new game`() {
    useServer(port = 45678) {
      val writer = StringWriter()

      val cli = GameCli.new(writer, handler = it)
      cli.parse("config", "--reset")
      cli.parse("new-game", "foo")

      writer.toString() shouldContain "Created game 'foo'."
    }
  }

  @Test
  fun `show a game`() {
    useServer(port = 45678) {
      val writer = StringWriter()

      val cli = GameCli.new(writer, handler = it)
      cli.parse("config", "--reset")
      cli.parse("new-game", "foo")
      cli.parse("show", "foo")

      writer.toString() shouldNotBe ""
    }
  }
}
