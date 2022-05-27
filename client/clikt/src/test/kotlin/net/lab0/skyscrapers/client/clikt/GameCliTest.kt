package net.lab0.skyscrapers.client.clikt

import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.client.FakeServerTest
import net.lab0.skyscrapers.server.ServiceImpl
import org.junit.jupiter.api.Test
import java.io.StringWriter

internal class GameCliTest : FakeServerTest {
  @Test
  fun `connection integration test`() {

    fakeServer {
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
    fakeServer {
      val writer = StringWriter()

      val cli = GameCli.new(writer, handler = it)
      cli.parse("config", "--reset")
      cli.parse("new-game", "foo")

      writer.toString() shouldContain "Created game 'foo'."
    }
  }

  @Test
  fun `show a game`() {
    val service = ServiceImpl.new()
    service.createGame(GameName("foo"))

    fakeServer(service = service) {
      val writer = StringWriter()

      val cli = GameCli.new(writer, handler = it)
      cli.parse("config", "--reset")
      cli.parse("show", "foo")

      writer.toString() shouldNotBe ""
    }
  }

  @Test
  fun `place a builder`() {
    val service = ServiceImpl.new()
    service.createGame(GameName("foo"))

    fakeServer(service = service) {
      val writer = StringWriter()

      val cli = GameCli.new(writer, handler = it)
      cli.parse("config", "--reset")
      cli.parse("play", "game", "foo", "place", "at", "0,0")

      writer.toString() shouldNotBe ""
    }
  }
}
