package net.lab0.skyscrapers.engine.api

import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.GameFactoryImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class SeriesImplTest {
  @Test
  fun `dont act as long as no game was started`() {
    val series = SeriesImpl(9)
    series.withGame { play(TurnType.GiveUpTurn(0)) }
    // no round yet
    assertThat(series.currentRound).isNull()
    assertThat(series.rounds).isEqualTo(0)
  }

  @Test
  fun `can start a new round`() {
    val series = SeriesImpl(9)
    series.start()
    series.withGame { play(TurnType.PlacementTurn(0, Position(0, 0))) }

    assertThat(series.currentRound).isEqualTo(0)
    assertThat(series.rounds).isEqualTo(1)
    assertThat(
      series.getRound(0)?.state?.hasBuilder(Position(0, 0))
    ).isTrue()
  }

  @Test
  fun `can't start a new round while the current one is ongoing`() {
    val series = SeriesImpl(9)
    series.start()
    series.withGame { play(TurnType.PlacementTurn(0, Position(0, 0))) }

    assertThat(
      assertThrows<IllegalStateException> {
        series.start()
      }
    ).hasMessage("Can't start a new game as the current one is not finished. It's time for player #1 to play.")
  }

  @Test
  fun `can stop a round by winning`() {
    val series = SeriesImpl(9)
    series.start()
    series.withGame { play(TurnType.GiveUpTurn(0)) }

    assertThat(series.currentRound).isNull() //  no active round
    assertThat(series.rounds).isEqualTo(1)
  }

  @Test
  fun `can't start a game when the series has been won`() {
    val bestOf1 = SeriesImpl(1)
    bestOf1.start()
    bestOf1.withGame { play(TurnType.GiveUpTurn(0)) }

    assertThat(bestOf1.isFinished()).isTrue()

    assertThat(
      assertThrows<IllegalStateException> {
        bestOf1.start()
      }
    ).hasMessage("Can't start a new game when the series was won: the winner is player #1")
  }

  @Test
  fun `can start a second round`() {
    val series = SeriesImpl(9)
    series.start()
    series.withGame { play(TurnType.GiveUpTurn(0)) }

    assertThat(series.isFinished()).isFalse()

    series.start()
    series.withGame { play(TurnType.GiveUpTurn(0)) }

    assertThat(series.rounds).isEqualTo(2)
  }

  @Test
  fun `can access the games history`() {
    val series = SeriesImpl(9)
    series.start()
    series.withGame { play(TurnType.GiveUpTurn(0)) }
    series.start()
    series.withGame { play(TurnType.PlacementTurn(0, Position(0, 0))) }
    series.withGame { play(TurnType.GiveUpTurn(1)) }

    assertThat(series.getRound(0)?.state?.winner).isEqualTo(1)
    assertThat(series.getRound(1)?.state?.winner).isEqualTo(0)
  }

  @Test
  fun `can customize the new game with a factory`() {
    val factory = { GameFactoryImpl().new(2, 2, 2, 1) }
    val series = SeriesImpl(1, factory)
    series.start()
    assertThat(series.getCurrentRound()?.state).isEqualTo(factory().state)
  }
}
