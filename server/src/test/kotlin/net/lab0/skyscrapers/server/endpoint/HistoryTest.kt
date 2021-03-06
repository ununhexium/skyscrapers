package net.lab0.skyscrapers.server.endpoint

import arrow.core.Either.Right
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import net.lab0.skyscrapers.api.dto.HistoryResponseDTO
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.routed
import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.format.KotlinxSerialization.auto
import org.junit.jupiter.api.Test

internal class HistoryTest {

  @Test
  fun `get history`() {
    val gameName = GameName("foo")
    val service = mockk<Service> {
      every { getGameHistory(gameName) } returns
          Right(History.Examples.okHistory)
    }

    val req = Request(Method.GET, "/api/v1/games/foo/history")
    val response = routed(service)(req)

    response.status shouldBe Status.OK

    val body = Body.auto<HistoryResponseDTO>().toLens()

    body(response).toModel() shouldBe History.Examples.okHistory
  }
}
