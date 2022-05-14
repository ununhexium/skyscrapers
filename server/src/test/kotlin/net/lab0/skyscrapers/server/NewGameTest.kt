package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.logic.api.Game
import org.assertj.core.api.Assertions.assertThat
import org.http4k.asString
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Test

internal class NewGameTest {
  @Test
  fun `creates a new game when requesting new game`() {

    // given
    val games = mutableMapOf<String, Game>()

    //when
    val created = routed(games)(Request(Method.GET, "/new/gameName"))
    val game = games["gameName"]
    assertThat(game).isNotNull
    assertThat(created.status).isEqualTo(Status.CREATED)

    val alreadyExists = routed(games)(Request(Method.GET, "/new/gameName"))
    assertThat(alreadyExists.body.payload.asString()).isEqualTo("The game gameName already exists.")
  }
}
