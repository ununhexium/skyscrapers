package net.lab0.skyscrapers.client.shell.spring

import arrow.core.Either
import arrow.core.Either.Right
import com.ninjasquad.springmockk.SpykBean
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.ConnectionResponse
import net.lab0.skyscrapers.api.dto.GameResponse
import net.lab0.skyscrapers.api.dto.StatusResponse
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.BlocksData
import net.lab0.skyscrapers.api.structure.Bounds
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.Height
import net.lab0.skyscrapers.api.structure.Matrix
import net.lab0.skyscrapers.api.structure.Player
import net.lab0.skyscrapers.client.http.SkyscraperClient
import net.lab0.skyscrapers.client.shell.spring.component.ServerAccessManager
import net.lab0.skyscrapers.client.shell.spring.data.ShellResult
import org.http4k.core.Status
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.shell.Shell
import org.springframework.shell.jline.InteractiveShellApplicationRunner
import org.springframework.shell.jline.ScriptShellApplicationRunner
import org.springframework.shell.result.DefaultResultHandler
import org.springframework.test.context.junit.jupiter.SpringExtension


@SpringBootTest(
  properties = [
    InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
    ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT + ".enabled=false"
  ]
)
@ExtendWith(SpringExtension::class)
internal class MenuShellTest {

  companion object {
    val baseUrl = "http://localhost:45678/"
    val yggdrasil = "Yggdrasil"
    val game = GameName(yggdrasil)
    val token = AccessToken("TOKEN")
  }

  @SpykBean
  lateinit var factory: SkyscraperClientFactoryComponent

  @SpykBean
  lateinit var serverAccessManager: ServerAccessManager

  @Autowired
  private lateinit var shell: Shell

  @Autowired
  private lateinit var resultHandler: DefaultResultHandler

  @BeforeEach
  fun beforeEach() {
    clearAllMocks()
  }

  @Test
  fun `connect succeeds`() {
    val client = mockk<SkyscraperClient>() {
      every { status() } returns Right(
        StatusResponse(
          "up",
          setOf("foo", "bar")
        )
      )
    }

    every { factory.newClient(BaseUrl(baseUrl)) } returns client

    val connect = shell.evaluate { "connect --url $baseUrl" } as String
    resultHandler.handleResult(connect)
    connect shouldContain "Connected to $baseUrl."
    connect shouldContain "Available games:"
    connect shouldContain "foo"
    connect shouldContain "bar"

    serverAccessManager.isConnected() shouldBe true
  }

  // TODO: generic error display class
  @Test
  fun `connection fails`() {
    val status = Status.INTERNAL_SERVER_ERROR

    val client = mockk<SkyscraperClient>() {
      every { status() } returns Either.Left(status)
    }

    every { factory.newClient(BaseUrl(baseUrl)) } returns client

    val connect = shell.evaluate { "connect --url $baseUrl" } as String
    resultHandler.handleResult(connect)
    connect shouldContain "Failed to query $baseUrl with status $status."
  }

  @Test
  fun `create a game`() {
    val client = mockk<SkyscraperClient>() {
      every { create(game) } returns
          Right(GameResponse(game, GameState.DUMMY))
    }

    serverAccessManager.forceState(InternalGameAccessState(client = client))

    val create = shell.evaluate { "create --game Yggdrasil" } as ShellResult.Ok.Text
    resultHandler.handleResult(create)
    create.output shouldContain "Created the game Yggdrasil."
  }

  @Test
  fun `join a game`() {
    val client = mockk<SkyscraperClient>() {
      every { join(game) } returns
          Right(ConnectionResponse(0, AccessToken("TOKEN")))
    }

    serverAccessManager.forceState(InternalGameAccessState(client = client))

    val create = shell.evaluate { "join --game Yggdrasil" } as ShellResult.Ok.Text
    resultHandler.handleResult(create)
    create.output shouldContain "Joined the game Yggdrasil as player 0 with access token TOKEN."
  }

  @Test
  fun `show the state`() {
    val client = mockk<SkyscraperClient>() {
      every { state(game) } returns
          Right(
            GameState(
              Bounds(0..2, 0..1),
              listOf(Player(0, true), Player(1, false)),
              2,
              BlocksData(Height(0) to 4, Height(1) to 3),
              Matrix.from(
                """
                |0 1 0
                |2 0 0
              """.trimMargin()
              ) { Height(it.toInt()) },
              Matrix.from(
                """
                |. X .
                |. . X
              """.trimMargin()
              ) { it != "." },
              Matrix.from(
                """
                |. 1 .
                |. . 0
              """.trimMargin()
              ) { it.toIntOrNull() },
            )
          )
    }

    serverAccessManager.forceState(InternalGameAccessState(BaseUrl(""), client, game, token))

    val create = shell.evaluate { "state" } as String
    resultHandler.handleResult(create)
    create shouldContain "Board"
    // TODO: show the board with color using spring shell's annotated string
  }
}
