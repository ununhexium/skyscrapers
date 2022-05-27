package net.lab0.skyscrapers.server.endpoint

import io.kotest.matchers.should
import net.lab0.skyscrapers.server.ServiceImpl
import net.lab0.skyscrapers.api.dto.StatusResponse
import net.lab0.skyscrapers.server.haveResponseBe
import net.lab0.skyscrapers.server.routed
import net.lab0.skyscrapers.api.dto.value.GameName
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.junit.jupiter.api.Test

internal class StatusTest {
  @Test
  fun `can tell that the server is available and how many games are available`() {
    val service = ServiceImpl.new()

    val games = listOf(
      GameName("fubar1"),
      GameName("fubar2"),
      GameName("fubar3"),
    )

    games.forEach { service.createGame(it) }

    val status = routed(service)(Request(GET, "/api/v1/status"))
    status should haveResponseBe(
      StatusResponse("up", games.map { it.value }.toSet())
    )
  }
}
