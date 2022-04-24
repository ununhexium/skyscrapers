package net.lab0.skyscrapers

import net.lab0.skyscrapers.exception.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows

internal class GameImplTest {

  companion object {
    fun gameWithSequentiallyPlacedBuilders(
      width: Int = 5,
      height: Int = 5,
      playerCount: Int = 2,
      buildersPerPlayer: Int = 2
    ) = Game.new(width, height, playerCount, buildersPerPlayer).also { it ->
      (0 until playerCount * buildersPerPlayer).forEach { turn ->
        val player = turn % playerCount
        val x = turn % width
        val y = turn / height
        it.play {
          player(player) {
            addBuilder(x, y)
          }
        }
      }
    }


  }

  @Nested
  inner class Init {

    @Test
    fun `the game must have at least 1 player`() {
      assertThrows<InvalidPlayersCount> {
        Game.new(playerCount = 0)
      }
    }

    @Test
    fun `the game's board must be at least the number of builders + 1`() {
      assertThrows<InvalidBoardSize> {
        Game.new(width = 2, height = 2, playerCount = 2, buildersPerPlayer = 2)
      }

      assertThrows<InvalidBoardSize> {
        Game.new(
          width = -1,
          height = 10,
          playerCount = 2,
          buildersPerPlayer = 2
        )
      }

      /*
       * This is valid (although not playable)
       * as 9 builders is less than 10 board cells
       */
      Game.new(width = 1, height = 10, playerCount = 3, buildersPerPlayer = 3)
    }

    @TestFactory
    fun `the initial height of the buildings is 0`(): Iterable<DynamicTest> {
      val width = 3
      val height = 4
      val g: Game = Game.new(width, height)

      return (0 until width).flatMap { x ->
        (0 until height).map { y ->
          DynamicTest.dynamicTest("g[$x,$y] == 0") {
            assertThat(g[x, y]).isEqualTo(0)
          }
        }
      }
    }

    @Test
    fun `the game starts in the placement phase`() {
      val g = Game.new()
      assertThat(g.phase).isEqualTo(Phase.PLACEMENT)
    }

    @Test
    fun `the game board starts with 0 builders on it`() {
      val g: Game = Game.new(playerCount = 2, buildersPerPlayer = 1)
      assertThat(g.getBuilders(0)).hasSize(0)
      assertThat(g.getBuilders(1)).hasSize(0)
    }
  }

  @Nested
  inner class Placement {

    @Test
    fun `the game starts with the placing phase`() {
      val g = Game.new()
      assertThat(g.phase).isEqualTo(Phase.PLACEMENT)
    }

    @Test
    fun `the players can place their builder`() {
      val g: Game = Game.new()
      val player = 0

      g.play {
        player(player) {
          addBuilder(Position(1, 2))
        }
      }

      assertThat(g.getBuilders(player)).isEqualTo(listOf(Position(1, 2)))
    }

    @Test
    fun `players must add their builders in alternating turns`() {
      val g: Game = Game.new()

      g.play {
        player(0) {
          addBuilder(0, 0)
        }
      }

      assertThrows<WrongPlayerTurn> {
        g.play {
          player(0) {
            addBuilder(1, 1)
          }
        }
      }

      g.play {
        player(1) {
          addBuilder(1, 1)
        }
      }

      assertThrows<WrongPlayerTurn> {
        g.play {
          player(1) {
            addBuilder(2, 2)
          }
        }
      }
    }

    @Test
    fun `the game starts at turn 0`() {
      val g: Game = Game.new(playerCount = 2)
      assertThat(g.turn).isEqualTo(0)
      assertThat(g.currentPlayer).isEqualTo(0)
    }

    @Test
    fun `the turn increases each time a player plays`() {
      val g: Game = Game.new(playerCount = 2)

      g.play {
        player(0) {
          addBuilder(0, 0)
        }
      }

      assertThat(g.turn).isEqualTo(1)
      assertThat(g.currentPlayer).isEqualTo(1)

      g.play {
        player(1) {
          addBuilder(1, 1)
        }
      }

      assertThat(g.turn).isEqualTo(2)
      assertThat(g.currentPlayer).isEqualTo(0)
    }

    @Test
    fun `can't place a builder on top of another builder`() {
      val g = Game.new()

      g.play {
        player(0) {
          addBuilder(0, 0)
        }
      }

      assertThrows<CellUsedByAnotherBuilder> {
        g.play {
          player(1) {
            addBuilder(0, 0)
          }
        }
      }
    }

    @Test
    fun `the game phase changes to building once all the builders have been placed`() {
      val g = Game.new(playerCount = 2, buildersPerPlayer = 1)
      g.play {
        player(0) {
          addBuilder(0, 0)
        }
      }
    }

    @Test
    fun `the player can't give up in the placement phase`() {
      val g = Game.new()

      assertThrows<CantGiveUpInThePlacementPhase> {
        g.play {
          player(0) {
            giveUp()
          }
        }
      }
    }
  }

  @Nested
  inner class Building {

    @Test
    fun `when a player gives up, he can't play anymore`() {
      val g = gameWithSequentiallyPlacedBuilders(playerCount = 3, buildersPerPlayer = 2)

      assertThat(g.turn).isEqualTo(6)
      assertThat(g.currentPlayer).isEqualTo(0)

      g.play {
        player(0) {
          giveUp()
        }
      }
      assertThat(g.turn).isEqualTo(7)
      assertThat(g.currentPlayer).isEqualTo(1)

      g.play {
        player(1) {
          moveBuilder(Position(1,0), Position(5,5))
        }
      }

      assertThat(g.turn).isEqualTo(8)
      assertThat(g.currentPlayer).isEqualTo(2)

      g.play {
        player(2) {
          moveBuilder(Position(2,0), Position(4,5))
        }
      }

      // now the player 0 doesn't play because it gave up

      assertThat(g.turn).isEqualTo(9)
      assertThat(g.currentPlayer).isEqualTo(1)
    }
  }

  @Nested
  inner class Finished {
    @Test
    fun `when there is only 1 player remaining, the game is finished`() {
      val g = gameWithSequentiallyPlacedBuilders()

      assertThat(g.isFinished()).isFalse

      g.play {
        player(0) {
          giveUp()
        }
      }

      assertThat(g.isFinished()).isTrue
    }
  }
}
