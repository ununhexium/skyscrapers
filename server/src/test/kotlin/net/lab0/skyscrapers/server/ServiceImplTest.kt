package net.lab0.skyscrapers.server

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.ErrorMessage
import net.lab0.skyscrapers.api.structure.TurnType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ServiceImplTest {
  @Test
  fun `can create a non existing game`() {
    val name = GameName("foo")
    val service = ServiceImpl.new()
    service.createGameIfItDoesntExist(name).shouldBeRight()
  }

  @Test
  fun `can't create an already existing game`() {
    val name = GameName("foo")
    val service = ServiceImpl.new()
    service.createGameIfItDoesntExist(name).shouldBeRight()
    service.createGameIfItDoesntExist(name) shouldBeLeft
        ErrorMessage("The game foo already exists.")
  }

  @Test
  fun `can't get the state of a non existing game`() {
    val name = GameName("foo")
    val service = ServiceImpl.new()
//    service.createGame(name)
    val state = service.getGameState(name)
    state shouldBeLeft ErrorMessage("No game named 'foo'.")
  }

  @Test
  fun `can get the history of an existing game`() {
    val name = GameName("foo")
    val service = ServiceImpl.new()
    service.createGameIfItDoesntExist(name)
    val state = service.getGameHistory(name)
    state.shouldBeRight()
  }

  @Test
  fun `can't get the history of a non existing game`() {
    val name = GameName("foo")
    val service = ServiceImpl.new()
//    service.createGame(name)
    val state = service.getGameHistory(name)
    state shouldBeLeft ErrorMessage("No game named 'foo'.")
  }

  @Test
  fun `can't join a full game`() {
    val name = GameName("foo")
    val service = ServiceImpl.new()
    service.createGameIfItDoesntExist(name)

    val cnx0 = service.join(name).shouldBeRight()
    assertThat(cnx0.id).isEqualTo(0)
    val cnx1 = service.join(name).shouldBeRight()
    assertThat(cnx1.id).isEqualTo(1)

    service.join(name).shouldBeLeft() shouldBe JoiningError.GameIsFull
  }

  @Test
  fun `can get the game names`() {
    val names = listOf("1", "2", "abc").map { GameName(it) }
    val service = ServiceImpl.new()
    names.forEach { service.createGameIfItDoesntExist(it) }

    service.getGameNames() shouldBe names.toSet()
  }

  @Test
  fun `a player can play in a game that he joined`() {
    val service = ServiceImpl.new()
    val g = GameName("g")
    service.createGameIfItDoesntExist(g)
    val p0 = service.join(g).shouldBeRight()
    val p1 = service.join(g).shouldBeRight()

    service.canParticipate(g, p0.token) shouldBe true
    service.canParticipate(g, p1.token) shouldBe true
    service.canParticipate(GameName("Doesn't exist"), p1.token) shouldBe false
  }

  @Test
  fun `a player can't play in a game that he didn't join`() {
    val service = ServiceImpl.new()
    val a = GameName("A")
    val b = GameName("B")

    service.createGameIfItDoesntExist(a)
    service.createGameIfItDoesntExist(b)

    val p0 = service.join(a).shouldBeRight()
    val p1 = service.join(a).shouldBeRight()
    val p2 = service.join(b).shouldBeRight()
    val p3 = service.join(b).shouldBeRight()

    service.canParticipate(b, p0.token) shouldBe false
    service.canParticipate(b, p1.token) shouldBe false
    service.canParticipate(a, p2.token) shouldBe false
    service.canParticipate(a, p3.token) shouldBe false
  }

  @Test
  fun `can get the player's id based on their token`() {
    val service = ServiceImpl.new()
    val a = GameName("A")

    service.createGameIfItDoesntExist(a)

    val p0 = service.join(a).shouldBeRight()
    val p1 = service.join(a).shouldBeRight()

    service.getPlayerId(a, p0.token) shouldBe 0
    service.getPlayerId(a, p1.token) shouldBe 1
  }

  @Test
  fun `can play an existing game`() {
    val name = GameName("foo")
    val service = ServiceImpl.new()
    service.createGameIfItDoesntExist(name)
    val state = service.playGame(name, TurnType.GiveUpTurn(0))
    state.shouldBeRight()
  }

  @Test
  fun `can't play a non existing game`() {
    val name = GameName("foo")
    val service = ServiceImpl.new()
//    service.createGameIfItDoesntExist(name)
    val state = service.playGame(name, TurnType.GiveUpTurn(0))
    state shouldBeLeft ErrorMessage("No game named 'foo'.")
  }

  @Test
  fun `return the errors when playing an invalid turn`() {
    val name = GameName("foo")
    val service = ServiceImpl.new()
//    service.createGameIfItDoesntExist(name)
    val state = service.playGame(name, TurnType.GiveUpTurn(0))
    state shouldBeLeft ErrorMessage("No game named 'foo'.")
  }
}
