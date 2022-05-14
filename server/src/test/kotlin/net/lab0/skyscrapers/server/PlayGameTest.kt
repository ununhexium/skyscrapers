package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.logic.api.Game
import net.lab0.skyscrapers.server.value.GameName
import org.http4k.core.Method.PUT
import org.http4k.core.Request
import org.http4k.core.with
import org.junit.jupiter.api.Test

internal class PlayGameTest {
  @Test
  fun `can place a builder`() {
    val service = ServiceImpl.new(mapOf(GameName("foo") to Game.new()))
    val response = routed(service)(
      Request(PUT, "/api/v1/games/foo/play").with(
//        Body.auto<>()
      )
    )
  }
}