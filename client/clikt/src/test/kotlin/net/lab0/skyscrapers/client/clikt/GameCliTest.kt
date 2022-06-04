package net.lab0.skyscrapers.client.clikt

import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldMatch
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
  fun `join a game`() {
    val service = ServiceImpl.new()
    service.createGame(GameName("foo"))

    fakeServer(service = service) {
      val writer = StringWriter()

      val cli = GameCli.new(writer, handler = it)
      cli.parse("config", "--reset")
      cli.parse("join", "foo")
      cli.parse("current")

      writer.toString() shouldMatch Regex(
        ".*Joined the game foo as \\p{XDigit}{8}-(\\p{XDigit}{4}-){3}\\p{XDigit}{12}.*",
        RegexOption.DOT_MATCHES_ALL
      )
      writer.toString() shouldMatch Regex(
        ".*The current game is foo and the token for it is \\p{XDigit}{8}-(\\p{XDigit}{4}-){3}\\p{XDigit}{12}.*",
        RegexOption.DOT_MATCHES_ALL
      )
    }
  }

  @Test
  fun `place a builder`() {
    val service = ServiceImpl.new()
    val game = GameName("foo")
    service.createGame(game)

    fakeServer(service = service) {
      val writer = StringWriter()

      val cli = GameCli.new(writer, handler = it)
      cli.parse("config", "--reset")
      cli.parse("join", "foo")

      val p0 = service.join(game)

      cli.parse("play", "place", "at", "0,0")

      writer.toString() shouldNotBe ""
    }
  }
}
