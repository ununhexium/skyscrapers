package net.lab0.skyscrapers.client.shell.spring

import arrow.core.Either
import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import com.ninjasquad.springmockk.clear
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.mockk.clearAllMocks
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.ConnectionResponse
import net.lab0.skyscrapers.api.dto.GameResponse
import net.lab0.skyscrapers.api.dto.StatusResponse
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.client.http.SkyscraperClient
import net.lab0.skyscrapers.client.shell.spring.component.GameMaster
import net.lab0.skyscrapers.engine.GameFactoryImpl
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
internal class MenuTest {

  @SpykBean
  lateinit var factory: SkyscraperClientFactoryComponent

  @SpykBean
  lateinit var gameMaster: GameMaster

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
    val baseUrl = "http://localhost:45678/"

    val client = mockk<SkyscraperClient>() {
      every { status() } returns Either.Right(
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

    gameMaster.state.client shouldNotBe null
  }

  // TODO: generic error display class
  @Test
  fun `connection fails`() {
    val baseUrl = "http://localhost:45678/"
    val status = Status.INTERNAL_SERVER_ERROR

    val client = mockk<SkyscraperClient>() {
      every { status() } returns Either.Left(status)
    }

    every { factory.newClient(BaseUrl(baseUrl)) } returns client

    val connect = shell.evaluate { "connect --url $baseUrl" } as String
    resultHandler.handleResult(connect)
    connect shouldContain "Failed to connect to $baseUrl with status $status."
  }

  @Test
  fun `create a game`() {
    val baseUrl = "http://localhost:45678/"
    val yggdrasil = "Yggdrasil"
    val game = GameName(yggdrasil)

    val client = mockk<SkyscraperClient>() {
      every { create(game) } returns Either.Right(
        GameResponse(
          game,
          GameFactoryImpl().new().state
        )
      )
    }

    gameMaster.forceState(ShellState(client))

    val create = shell.evaluate { "create --game $yggdrasil" } as String
    resultHandler.handleResult(create)
    create shouldContain "Created game $yggdrasil."
  }

  @Test
  fun `join a game`() {
    val baseUrl = "http://localhost:45678/"
    val yggdrasil = "Yggdrasil"
    val game = GameName(yggdrasil)

    val client = mockk<SkyscraperClient>() {
      every { join(game) } returns Either.Right(
        ConnectionResponse(0, AccessToken("TOKEN"))
      )
    }

    gameMaster.forceState(ShellState(client))

    val create = shell.evaluate { "join --game $yggdrasil" } as String
    resultHandler.handleResult(create)
    create shouldContain "Joined game $yggdrasil as player 0 with access token TOKEN."
  }

  @Test
  fun `place a builder`() {
    val baseUrl = "http://localhost:45678/"
    val yggdrasil = "Yggdrasil"
    val game = GameName(yggdrasil)
    val token = AccessToken("TOKEN")

    val client = mockk<SkyscraperClient>() {
      every { place(game, token, Position(0, 0)) } returns Either.Right(
        GameFactoryImpl().new().state
      )
    }

    gameMaster.forceState(ShellState(client, game, token))

    val evaluation = shell.evaluate { "place --at 0,0" }
    val create = evaluation as String
    resultHandler.handleResult(create)
    create shouldContain "Placed a builder at 0,0."
  }

  @Test
  fun build() {
    val baseUrl = "http://localhost:45678/"
    val yggdrasil = "Yggdrasil"
    val game = GameName(yggdrasil)
    val token = AccessToken("TOKEN")

    val start = Position(0, 0)
    val target = Position(1, 1)
    val build = Position(2, 2)

    val client = mockk<SkyscraperClient>() {
      every { status() } returns Either.Left(Status.OK)

      every { build(game, token, start, target, build) } returns Either.Right(
        GameFactoryImpl().new().state
      )
    }

    every { factory.newClient(BaseUrl(baseUrl)) } returns client

    gameMaster.forceState(
      ShellState(client, game, token)
    )

    shell.evaluate { "connect --url $baseUrl" }
    val create =
      shell.evaluate { "build --from 0,0 --to 1,1 --build 2,2" } as String
    resultHandler.handleResult(create)
    create shouldContain "Moved builder from 0,0 to 1,1 and built at 2,2."
  }

}
