package net.lab0.skyscrapers.client.shell.spring

import arrow.core.Either.Right
import com.ninjasquad.springmockk.SpykBean
import io.kotest.matchers.should
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.beInstanceOf
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.BlocksData
import net.lab0.skyscrapers.api.structure.Bounds
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.Height
import net.lab0.skyscrapers.api.structure.Matrix
import net.lab0.skyscrapers.api.structure.Player
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.api.structure.Position.Style.COMA
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.api.structure.TurnType.MoveTurn.BuildTurn
import net.lab0.skyscrapers.api.structure.TurnType.MoveTurn.SealTurn
import net.lab0.skyscrapers.client.http.SkyscraperClient
import net.lab0.skyscrapers.client.shell.spring.component.ServerAccessManager
import net.lab0.skyscrapers.client.shell.spring.data.ShellResult
import net.lab0.skyscrapers.client.shell.spring.data.ShellResult.Ok.StateUpdate
import net.lab0.skyscrapers.engine.Defaults
import org.jline.terminal.Terminal
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.shell.CompletionContext
import org.springframework.shell.CompletionProposal
import org.springframework.shell.Shell
import org.springframework.shell.jline.InteractiveShellApplicationRunner
import org.springframework.shell.jline.ScriptShellApplicationRunner
import org.springframework.shell.result.DefaultResultHandler
import org.springframework.shell.table.Table
import org.springframework.test.context.junit.jupiter.SpringExtension


@SpringBootTest(
  properties = [
    InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
    ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT + ".enabled=false"
  ]
)
@ExtendWith(SpringExtension::class)
internal class GameShellTest /* TODO extract ShellTest() */ {

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

  @SpykBean
  lateinit var terminal: Terminal

  @Autowired
  private lateinit var shell: Shell

  @Autowired
  private lateinit var resultHandler: DefaultResultHandler

  private val emptyState = GameState(
    Bounds(0 until Defaults.WIDTH, 0 until Defaults.HEIGHT),
    listOf(Player(0, true), Player(1, true)),
    2,
    BlocksData(
      Height(0) to 17,
      Height(1) to 15,
      Height(2) to 10,
      Height(3) to 5,
    ),
    Matrix(Defaults.HEIGHT, Defaults.WIDTH) { Height(0) },
    Matrix(Defaults.HEIGHT, Defaults.WIDTH) { false },
    Matrix(Defaults.HEIGHT, Defaults.WIDTH) { null },
  )

  @BeforeEach
  fun beforeEach() {
    clearAllMocks()
    every { terminal.writer() } returns mockk {
      every { flush() } returns Unit
      every { println(any<Table>()) } returns Unit
      every { println(any<String>()) } returns Unit
    }
  }

  // TODO: extract to shell test
  final inline fun <reified T : Any> eval(input: String): T {
    val evaluation = shell.evaluate { input }
    resultHandler.handleResult(evaluation)
    evaluation should beInstanceOf<T>()
    return evaluation as T
  }

  fun Shell.complete(vararg args: String) =
    this.complete(CompletionContext(args.toList(), args.toList().size, 0))

  @Test
  fun `place a builder`() {
    val client = mockk<SkyscraperClient>() {
      every { place(game, token, Position(0, 0)) } returns
          Right(emptyState)
    }

    serverAccessManager.forceState(
      InternalGameAccessState(
        BaseUrl(""),
        client,
        0,
        game,
        token
      )
    )

    val create = eval<StateUpdate>("place --at 0,0")

    create.comment shouldContain "Placed a builder at 0,0."
  }

  @Test
  fun `propose a place for the builder`() {
    val completions = listOf(Position(-1, -2), Position(-3, -4))

    every { serverAccessManager.placeAtCompletion() } returns completions

    val completion = shell.complete("place", "--at")
    completion should haveTextCompletions(
      completions.map { it.toString(COMA) }
    )
  }

  @Test
  fun build() {
    val start = Position(0, 0)
    val target = Position(1, 1)
    val build = Position(2, 2)

    val client = mockk<SkyscraperClient>() {
      every { build(game, token, start, target, build) } returns
          Right(GameState.DUMMY)
    }

    serverAccessManager.forceState(
      InternalGameAccessState(BaseUrl(""), client, 0, game, token)
    )

    val create =
      shell.evaluate { "build --from 0,0 --to 1,1 --build 2,2" } as String
    resultHandler.handleResult(create)
    create shouldContain "Moved builder from 0,0 to 1,1 and built at 2,2."
  }

  @Test
  fun `propose a start location in build`() {
    every {
      serverAccessManager.buildCompletion(null, null, null)
    } returns listOf(
      BuildTurn(
        0,
        Position(100, 100),
        Position(200, 200),
        Position(300, 300)
      )
    )
    val completion = shell.complete("build", "--from")
    completion should haveCompletions(
      CompletionProposal("100,100")
    )
  }

  @Test
  fun `propose a target location in build`() {
    every {
      serverAccessManager.buildCompletion(Position(100, 100), null, null)
    } returns listOf(
      BuildTurn(
        0,
        Position(100, 100),
        Position(200, 200),
        Position(300, 300)
      )
    )
    val completion = shell.complete("build", "--from", "100,100", "--to")
    completion should haveCompletions(
      CompletionProposal("200,200")
    )
  }

  @Test
  fun `propose a build location in build`() {
    every {
      serverAccessManager.buildCompletion(
        Position(100, 100),
        Position(200, 200),
        null
      )
    } returns listOf(
      BuildTurn(
        0,
        Position(100, 100),
        Position(200, 200),
        Position(300, 300)
      )
    )
    val completion =
      shell.complete("build", "--from", "100,100", "--to", "200,200", "--build")
    completion should haveCompletions(
      CompletionProposal("300,300")
    )
  }

  @Test
  fun seal() {
    val start = Position(0, 0)
    val target = Position(1, 1)
    val seal = Position(2, 2)

    val client = mockk<SkyscraperClient>() {
      every { seal(game, token, start, target, seal) } returns
          Right(GameState.DUMMY)
    }

    serverAccessManager.forceState(
      InternalGameAccessState(BaseUrl(""), client, 0, game, token)
    )

    val create =
      shell.evaluate { "seal --from 0,0 --to 1,1 --seal 2,2" } as String
    resultHandler.handleResult(create)
    create shouldContain "Moved builder from 0,0 to 1,1 and sealed at 2,2."
  }

  @Test
  fun `propose a start location in seal`() {
    every {
      serverAccessManager.sealCompletion(null, null, null)
    } returns listOf(
      SealTurn(
        0,
        Position(100, 100),
        Position(200, 200),
        Position(300, 300),
      )
    )
    val completion = shell.complete("seal", "--from")
    completion should haveCompletions(
      CompletionProposal("100,100")
    )
  }

  @Test
  fun `propose a target location in seal`() {
    every {
      serverAccessManager.sealCompletion(Position(100, 100), null, null)
    } returns listOf(
      SealTurn(
        0,
        Position(100, 100),
        Position(200, 200),
        Position(300, 300),
      )
    )
    val completion = shell.complete("seal", "--from", "100,100", "--to")
    completion should haveCompletions(
      CompletionProposal("200,200")
    )
  }

  @Test
  fun `propose a seal location in seal`() {
    every {
      serverAccessManager.sealCompletion(
        Position(100, 100),
        Position(200, 200),
        null
      )
    } returns listOf(
      SealTurn(
        0,
        Position(100, 100),
        Position(200, 200),
        Position(300, 300),
      )
    )
    val completion =
      shell.complete("seal", "--from", "100,100", "--to", "200,200", "--seal")
    completion should haveCompletions(
      CompletionProposal("300,300")
    )
  }

  @Test
  fun win() {
    val start = Position(0, 0)
    val target = Position(1, 1)

    val client = mockk<SkyscraperClient>() {
      every { win(game, token, start, target) } returns
          Right(GameState.DUMMY)
    }

    serverAccessManager.forceState(
      InternalGameAccessState(BaseUrl(""), client, 0, game, token)
    )

    val create =
      shell.evaluate { "win --from 0,0 --to 1,1" } as String
    resultHandler.handleResult(create)
    create shouldContain "Moved builder from 0,0 to 1,1 and won."
  }

  @Test
  fun history() {
    serverAccessManager.forceState(
      InternalGameAccessState(BaseUrl(""), null, 0, game, token)
    )

    every { serverAccessManager.history() } returns ShellResult.Ok.Text("The history contains 10 steps.")

    val create = shell.evaluate { "game-history" } as ShellResult.Ok.Text

    create.output shouldContain "The history contains 10 steps."

    verify(atLeast = 1) {
      serverAccessManager.history()
    }
  }
}
