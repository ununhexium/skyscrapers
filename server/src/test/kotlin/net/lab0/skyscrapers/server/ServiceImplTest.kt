package net.lab0.skyscrapers.server

import io.kotest.matchers.shouldBe
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.server.exception.GameFullException
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

    val cnx0 = service.join(name)
    assertThat(cnx0.id).isEqualTo(0)
    val cnx1 = service.join(name)
    assertThat(cnx1.id).isEqualTo(1)

    assertThat(
      assertThrows<GameFullException> {
        service.join(name)
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

  @Test
  fun `a player can play in a game that he joined`() {
    val service = ServiceImpl.new()
    val g = GameName("g")
    service.createGame(g)
    val p0 = service.join(g)
    val p1 = service.join(g)

    service.canParticipate(g, p0.token) shouldBe true
    service.canParticipate(g, p1.token) shouldBe true
    service.canParticipate(GameName("Doesn't exist"), p1.token) shouldBe false
  }

  @Test
  fun `a player can't play in a game that he didn't join`() {
    val service = ServiceImpl.new()
    val a = GameName("A")
    val b = GameName("B")

    service.createGame(a)
    service.createGame(b)

    val p0 = service.join(a)
    val p1 = service.join(a)
    val p2 = service.join(b)
    val p3 = service.join(b)

    service.canParticipate(b, p0.token) shouldBe false
    service.canParticipate(b, p1.token) shouldBe false
    service.canParticipate(a, p2.token) shouldBe false
    service.canParticipate(a, p3.token) shouldBe false
  }

  @Test
  fun `can get the player's id based on their token`() {
    val service = ServiceImpl.new()
    val a = GameName("A")

    service.createGame(a)

    val p0 = service.join(a)
    val p1 = service.join(a)

    service.getPlayerId(a, p0.token) shouldBe 0
    service.getPlayerId(a, p1.token) shouldBe 1
  }
}
