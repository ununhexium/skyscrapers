package net.lab0.skyscrapers.client.clikt

import com.github.ajalt.clikt.core.CliktCommand
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotContain
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.client.FakeServerTest
import net.lab0.skyscrapers.server.ServiceImpl
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.koin.test.mock.declare
import java.io.StringWriter

internal class GameCliTest :
  KoinBase(),
  FakeServerTest {

  @Test
  fun `connection integration test`() {

    fakeServer {
      val writer = StringWriter()

      declare { it }

      val cli = GameCli.new(writer)
      cli.parse("config", "--reset")
      cli.parse("connect")

      writer.toString() shouldNotContain "Failed to connect to the server"
      writer.toString() shouldContain "Connected."
    }
  }

  @Test
  fun `create a new game`() {
    fakeServer {
      declare { it }

      val writer = StringWriter()
      val cli = GameCli.new(writer)

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
      declare { it }
      val writer = StringWriter()

      val cli = GameCli.new(writer)
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
      declare { it }
      val writer = StringWriter()

      val cli = GameCli.new(writer)
      cli.parse("config", "--reset")
      cli.parse("join", "foo")
      cli.parse("current", "foo")

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
      declare { it }
      val writer = StringWriter()

      val cli = GameCli.new(writer)
      cli.parse("config", "--reset")
      cli.parse("join", "foo")

      val p0 = service.join(game)

      cli.parse("place", "--game", "foo", "--at", "0,0")

      writer.toString() shouldContain "Placed a builder at 0,0"
    }
  }

  @Test
  fun `place a builder again shows the violated rule`() {
    val service = ServiceImpl.new()
    val game = GameName("foo")
    service.createGame(game)

    fakeServer(service = service) {
      declare { it }
      val writer = StringWriter()

      val cli = GameCli.new(writer)
      cli.parse("config", "--reset")
      cli.parse("join", "foo")

      val p0 = service.join(game)

      cli.parse("place", "--game", "foo", "--at", "0,0")
      cli.parse("place", "--game", "foo", "--at", "0,1")

      writer.toString() shouldContain "Game rule violated."
      writer.toString() shouldContain "Name:"
      writer.toString() shouldContain "Description:"
      writer.toString() shouldContain "Detail:"
    }
  }

  private fun doDefaultPlacement(
    cli: CliktCommand,
    service: ServiceImpl,
    nameName: GameName
  ) {
    cli.parse("config", "--reset")
    cli.parse("join", "foo")

    val p1 = service.join(nameName).shouldBeRight()

    cli.parse("place", "--game", "foo", "--at", "0,0")

    val game = service.getGame(nameName).shouldBeRight()
    game.play(TurnType.PlacementTurn(p1.id, Position(0, 1)))

    cli.parse("place", "--game", "foo", "--at", "0,2")

    game.play(TurnType.PlacementTurn(p1.id, Position(0, 3)))
  }

  @Test
  fun `build on a cell`() {
    val service = ServiceImpl.new()
    val game = GameName("foo")
    service.createGame(game)

    fakeServer(service = service) {
      declare { it }
      val writer = StringWriter()

      val cli = GameCli.new(writer)

      doDefaultPlacement(cli, service, game)

      cli.parse(
        "build",
        "--game",
        "foo",
        "--from",
        "0,2",
        "--to",
        "1,3",
        "--build",
        "2,4"
      )

      writer.toString() shouldContain "Moved builder from 0,2 to 1,3 and built at 2,4"
    }
  }

  @Test
  fun `seal a cell`() {
    val service = ServiceImpl.new()
    val game = GameName("foo")
    service.createGame(game)

    fakeServer(service = service) {
      declare { it }
      val writer = StringWriter()

      val cli = GameCli.new(writer)

      doDefaultPlacement(cli, service, game)

      cli.parse(
        "seal",
        "--game",
        "foo",
        "--from",
        "0,2",
        "--to",
        "1,3",
        "--seal",
        "2,4"
      )

      writer.toString() shouldContain "Moved builder from 0,2 to 1,3 and sealed at 2,4"
    }
  }

  @Disabled("Needs a backdoor to test it in reasonable time.")
  @Test
  fun `win a turn`() {
    val service = ServiceImpl.new()
    val game = GameName("foo")
    service.createGame(game)

    fakeServer(service = service) {
      declare { it }
      val writer = StringWriter()

      val cli = GameCli.new(writer)

      doDefaultPlacement(cli, service, game)

      cli.parse(
        "win",
        "--game",
        "foo",
        "--from",
        "0,2",
        "--to",
        "1,3"
      )

      writer.toString() shouldContain "Moved builder from 0,2 to 1,3 and sealed at 2,4"
    }
  }
}
