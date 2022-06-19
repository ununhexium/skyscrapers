package net.lab0.skyscrapers.server.endpoint

import arrow.core.Either.Right
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import net.lab0.skyscrapers.api.dto.HistoryResponseDTO
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.engine.GameFactoryImpl
import net.lab0.skyscrapers.server.ServiceImpl
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
    val service = spyk(
      ServiceImpl.new(
        mapOf(gameName to GameFactoryImpl().new())
      )
    )

    val state = GameState.DUMMY
    every { service.getGame(gameName) } returns Right(
      mockk {
        every { history } returns listOf(state)
      }
    )

    val req = Request(Method.GET, "/api/v1/games/foo/history")
    val response = routed(service)(req)

    response.status shouldBe Status.OK

    val historyResponse = Body.auto<HistoryResponseDTO>().toLens().extract(response)
    historyResponse.history.size shouldBe 1
    historyResponse.history.first().toModel() shouldBe state
  }
}
