package net.lab0.skyscrapers.client.shell.spring

import com.ninjasquad.springmockk.MockkBean
import io.kotest.matchers.string.shouldContain
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.verify
import net.lab0.skyscrapers.client.shell.spring.component.ServerStateComponent
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
class ServerShellTest {

  @Autowired
  private lateinit var shell: Shell

  @Autowired
  private lateinit var resultHandler: DefaultResultHandler

  @MockkBean
  lateinit var state: ServerStateComponent

  @BeforeEach
  fun beforeEach() {
    clearAllMocks()
  }

  @Test
  fun `start a server`() {
    every { state.isRunning } returns false
    every { state.startServer(45678) } returns Unit

    val evaluation = shell.evaluate { "start-server --port 45678" }
    val connect = evaluation as String
    resultHandler.handleResult(connect)
    connect shouldContain "Server started on port 45678"

    verify { state.startServer(45678) }
  }

  @Test
  fun `server already started`() {
    every { state.isRunning } returns true
    every { state.startServer(45678) } returns Unit

    shell.evaluate { "start-server --port 45678" }
    val evaluation = shell.evaluate { "start-server --port 45678" }
    val connect = evaluation as String
    resultHandler.handleResult(connect)
    connect shouldContain "The server is already started."
  }

  @Test
  fun `stop server`() {
    every { state.isRunning } returns true
    every { state.stopServer() } returns Unit

    val evaluation = shell.evaluate { "stop-server" }
    val connect = evaluation as String
    resultHandler.handleResult(connect)
    connect shouldContain "Server stopped."

    verify { state.stopServer() }
  }

  @Test
  fun `server already stopped`() {
    every { state.isRunning } returns false
    every { state.stopServer() } returns Unit

    val evaluation = shell.evaluate { "stop-server" }
    val connect = evaluation as String
    resultHandler.handleResult(connect)
    connect shouldContain "The server is already stopped."
  }
}
