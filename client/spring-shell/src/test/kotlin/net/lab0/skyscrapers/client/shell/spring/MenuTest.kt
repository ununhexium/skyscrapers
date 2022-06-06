package net.lab0.skyscrapers.client.shell.spring

import arrow.core.Either
import com.ninjasquad.springmockk.MockkBean
import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.mockk
import net.lab0.skyscrapers.api.dto.StatusResponse
import net.lab0.skyscrapers.client.http.SkyscraperClient
import org.http4k.core.Status
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

  @MockkBean
  lateinit var factory: SkyscraperClientFactoryComponent

  @Autowired
  private lateinit var shell: Shell

  @Autowired
  private lateinit var resultHandler: DefaultResultHandler

  @Test
  fun connectSucceeds() {
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
  }

  @Test
  fun connectionFails() {
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

}
