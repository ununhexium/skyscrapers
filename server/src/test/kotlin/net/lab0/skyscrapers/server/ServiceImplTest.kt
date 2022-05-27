package net.lab0.skyscrapers.server

import io.kotest.matchers.shouldBe
import net.lab0.skyscrapers.server.exception.GameFullException
import net.lab0.skyscrapers.api.dto.value.GameName
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ServiceImplTest {
  @Test
  fun `can create and read a game`() {
    val name = GameName("foo")
    val service = ServiceImpl.new()
    val game = service.createGame(name)
    assertThat(game).isNotNull
    val sameGame = service.getGame(name)
    assertThat(game).isSameAs(sameGame)
  }

  @Test
  fun `can connect to a game`() {
    val name = GameName("foo")
    val service = ServiceImpl.new()
    service.createGame(name)

    val cnx0 = service.connect(name)
    assertThat(cnx0.player).isEqualTo(0)
    val cnx1 = service.connect(name)
    assertThat(cnx1.player).isEqualTo(1)

    assertThat(
      assertThrows<GameFullException> {
        service.connect(name)
      }
    ).hasMessage("The game foo is full.")
  }

  @Test
  fun `can get the game names`() {
    val names = listOf("1", "2", "abc").map { GameName(it) }
    val service = ServiceImpl.new()
    names.forEach { service.createGame(it) }

    service.getGameNames() shouldBe names.toSet()
  }
}