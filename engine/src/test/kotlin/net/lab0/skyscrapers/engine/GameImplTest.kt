package net.lab0.skyscrapers.engine

import net.lab0.skyscrapers.engine.DefaultGames.newGameWithSequentiallyPlacedBuilders
import net.lab0.skyscrapers.engine.action.DSL
import net.lab0.skyscrapers.engine.api.BlocksData
import net.lab0.skyscrapers.engine.api.Game
import net.lab0.skyscrapers.engine.api.GameState
import net.lab0.skyscrapers.engine.assertj.GameStateAssert
import net.lab0.skyscrapers.engine.exception.GameRuleViolationException
import net.lab0.skyscrapers.engine.exception.InvalidBlocksConfiguration
import net.lab0.skyscrapers.engine.exception.InvalidBoardSize
import net.lab0.skyscrapers.engine.exception.InvalidPlayersCount
import net.lab0.skyscrapers.engine.structure.BuildersMatrix
import net.lab0.skyscrapers.engine.structure.BuildingsMatrix
import net.lab0.skyscrapers.engine.structure.Height
import net.lab0.skyscrapers.engine.structure.Matrix
import net.lab0.skyscrapers.engine.structure.Phase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import net.lab0.skyscrapers.engine.structure.Position as P

internal class GameImplTest {

  @Nested
  inner class State {
    @Test
    fun `newGameWithSequentiallyPlacedBuilders generates this specific game by default`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      assertThat(g.state).isEqualTo(
        GameState.from(
          (0 until Defaults.PLAYER_COUNT).map { Player(it) },
          Defaults.BUILDERS_PER_PLAYER,
          Defaults.BLOCKS,
          """
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
          """.trimIndent(),

          """
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
          """.trimIndent(),

          """
            0 1 0 1 .
            . . . . .
            . . . . .
            . . . . .
            . . . . .
          """.trimIndent(),
        )
      )
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

      return (0 until width).flatMap { column ->
        (0 until height).map { row ->
          DynamicTest.dynamicTest("g[$row, $column] == 0") {
            assertThat(g.state.buildings[P(column, row)]).isEqualTo(Height(0))
          }
        }
      }
    }

    @Test
    fun `the game starts in the placement phase`() {
      val g = Game.new()
      assertThat(g.state.phase).isEqualTo(Phase.PLACEMENT)
    }

    @Test
    fun `the game board starts with 0 builders on it`() {
      val g: Game = Game.new(playerCount = 2, buildersPerPlayer = 1)
      assertThat(g.state.getBuilders(0)).hasSize(0)
      assertThat(g.state.getBuilders(1)).hasSize(0)
    }

    @Test
    fun `some blocks to build must be present`() {
      assertThat(
        assertThrows<InvalidBlocksConfiguration> {
          Game.new(blocks = BlocksData.EMPTY)
        }
      ).hasMessage("There must be at least 1 block for the game to make sense")

      assertThat(
        assertThrows<InvalidBlocksConfiguration> {
          Game.new(
            blocks = BlocksData(Height(2) to 0)
          )
        }
      ).hasMessage("There must be at least 1 block for the game to make sense")
    }

    @Test
    fun `there must be no gap the the blocks heights`() {
      assertThat(
        assertThrows<InvalidBlocksConfiguration> {
          Game.new(
            blocks = BlocksData(Height(1) to 10, Height(3) to 5)
          )
        }
      ).hasMessage("There must no gap in the blocks heights")
    }

    @Test
    fun `there must be more blocks of lower height than higher height`() {
      assertThat(
        assertThrows<InvalidBlocksConfiguration> {
          Game.new(
            blocks = BlocksData(Height(0) to 0, Height(1) to 1, Height(2) to 99)
          )
        }
      ).hasMessage("The lower blocks must be in larger quantity than the higher blocks")
    }

    @Test
    fun `each block must be proposed at least once`() {
      assertThat(
        assertThrows<InvalidBlocksConfiguration> {
          Game.new(
            blocks = BlocksData(
              Height(0) to 1,
              Height(1) to 1,
              Height(2) to 0,
              Height(3) to 0
            )
          )
        }
      ).hasMessage(
        "If a block is proposed, its initial quantity must be at least 1. No block of for the following heights: 2, 3"
      )
    }
  }

  @Nested
  inner class Placement {

    @Test
    fun `the game starts with the placing phase`() {
      val g = Game.new()
      assertThat(g.state.phase).isEqualTo(Phase.PLACEMENT)
    }

    @Test
    fun `the players can place their builder`() {
      val g: Game = Game.new()
      val player = 0

      g.play(
        DSL.player(player).placement.addBuilder(P(1, 2))
      )

      assertThat(g.state.getBuilders(player)).isEqualTo(listOf(P(1, 2)))
    }

    @Test
    fun `players must add their builders in alternating turns`() {
      val g: Game = Game.new()

      g.play(
        DSL.player(0).placement.addBuilder(0, 0)
      )

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(0).placement.addBuilder(1, 1)
        )
      }

      g.play(
        DSL.player(1).placement.addBuilder(1, 1)
      )

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(1).placement.addBuilder(2, 2)
        )
      }
    }

    @Test
    fun `the game starts at turn 0`() {
      val g: Game = Game.new(playerCount = 2)
      assertThat(g.turn).isEqualTo(0)
      assertThat(g.state.currentPlayer).isEqualTo(0)
    }

    @Test
    fun `the turn increases each time a player plays`() {
      val g: Game = Game.new(playerCount = 2)

      g.play(
        DSL.player(0).placement.addBuilder(0, 0)
      )

      assertThat(g.turn).isEqualTo(1)
      assertThat(g.state.currentPlayer).isEqualTo(1)

      g.play(
        DSL.player(1).placement.addBuilder(1, 1)
      )

      assertThat(g.turn).isEqualTo(2)
      assertThat(g.state.currentPlayer).isEqualTo(0)
    }

    @Test
    fun `can't place a builder on top of another builder`() {
      val g = Game.new()

      g.play(
        DSL.player(0).placement.addBuilder(0, 0)
      )

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(1).placement.addBuilder(0, 0)
        )
      }
    }

    @Test
    fun `the game phase changes to building once all the builders have been placed`() {
      val g = Game.new(playerCount = 2, buildersPerPlayer = 1)
      g.play(
        DSL.player(0).placement.addBuilder(0, 0)
      )
    }

    @Test
    fun `can't move during placement phase`() {
      val g = Game.new()

      // put a builder at 0,0
      g.play(
        DSL.player(0).placement.addBuilder(0, 0)
      )

      // the opponent plays
      g.play(
        DSL.player(1).placement.addBuilder(4, 4)
      )

      // can't move the first builder before placing them all
      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(0).building.move().from(0, 0).to(0, 1).andBuild(0, 0)
        )
      }
    }

    @Test
    fun `can't place outside of the board`() {
      val g = Game.new()

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(1).placement.addBuilder(5, 5)
        )
      }
    }
  }

  @Nested
  inner class Movement {

    @Test
    fun `the player can move a builder`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      val state0 = g.state

      g.play(
        DSL.player(0).building.move().from(0, 0).to(0, 1).andBuild(0, 0)
      )

      assertThat(g.state.getBuilders(0))
        .contains(P(0, 1), P(2, 0))

      GameStateAssert.assertThat(g.state).isEqualTo(
        state0.copy(
          players = listOf(Player(1), Player(0)),
          blocks = state0
            .blocks
            .removeBlockOfHeight(Height(1)),
          buildings = BuildingsMatrix.from(
            """
              |1 0 0 0 0
              |0 0 0 0 0
              |0 0 0 0 0
              |0 0 0 0 0
              |0 0 0 0 0
            """.trimMargin()
          ).matrix.map { Height(it) },
          builders = BuildersMatrix.from(
            """
              |. 1 0 1 .
              |0 . . . .
              |. . . . .
              |. . . . .
              |. . . . .
            """.trimMargin()
          ).matrix
        )
      )

    }

    @Test
    fun `the movement range of a builder is limited to the 8 cells around its initial position`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(0).building.move().from(0, 0).to(0, 2).andBuild(0, 1)
        )
      }
    }

    @Test
    fun `the player must move, it can't stay at the same place`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(0).building.move().from(0, 0).to(0, 0).andBuild(0, 1)
        )
      }
    }

    @Test
    fun `can't move another player's builder`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      val from = P(1, 0)
      val to = P(1, 1)

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(0).building.move().from(from).to(to).andBuild(1, 0)
        )
      }
    }

    @Test
    fun `can't move a builder that doesn't exist`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      val from = P(4, 4)
      val to = P(3, 3)

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(0).building.move().from(from).to(to).andBuild(4, 4)
        )
      }
    }

    @Test
    fun `can't play as a player that doesn't exist`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(99).building.move().from(0, 0).to(0, 1).andBuild(0, 0)
        )
      }
    }

    @Test
    fun `can't move on top of another player's builder`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(0).building.move().from(0, 0).to(1, 0).andBuild(0, 0)
        )
      }
    }

    @Test
    fun `can't move out of bounds`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(0).building.move().from(0, 0).to(-1, -1).andBuild(0, 0)
        )
      }
    }

    @Test
    fun `can't move from out of bounds`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      val start = P(-1, 0)
      val target = P(0, 0)

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(0).building.move().from(start).to(target).andBuild(1, 1)
        )
      }
    }

    @Test
    fun `can only move up 1 step at a time`() {
      val g = newGameWithSequentiallyPlacedBuilders() as GameImpl

      val start = P(0, 0)
      val end = P(1, 1)
      val build = P(0, 0)

      g.backdoor.forceState(g.state.height(end, 2)) // too high

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(0).building.move().from(start).to(end).andBuild(build)
        )
      }
    }
  }

  @Nested
  inner class Building {
    @Test
    fun `can build where there is no builder`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      val buildPosition = P(0, 0)

      g.play(
        DSL.player(0).building
          .move()
          .from(0, 0)
          .to(0, 1)
          .andBuild(buildPosition)
      )

      assertThat(g.state.buildings[buildPosition]).isEqualTo(Height(1))

      g.play(
        DSL.player(1).building
          .move()
          .from(1, 0)
          .to(1, 1)
          .andBuild(buildPosition)
      )

      // building stacks
      assertThat(g.state.buildings[buildPosition]).isEqualTo(Height(2))
    }

    @Test
    fun `can't build outside of the board`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      val builderStartPosition = P(0, 0)
      val builderTargetPosition = P(0, 1)
      val buildingPosition = P(-1, 1)

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(0).building
            .move()
            .from(builderStartPosition)
            .to(builderTargetPosition)
            .andBuild(buildingPosition)
        )
      }

      // the player's movement didn't occur either
      assertThat(g.state.hasBuilder(builderStartPosition)).isTrue()
      assertThat(g.state.hasBuilder(builderTargetPosition)).isFalse()
    }

    @Test
    fun `can't build where there is a builder on that tile`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      val builderStartPosition = P(0, 0)
      val builderTargetPosition = P(0, 1)
      val opponentPosition = P(1, 0)

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(0).building
            .move()
            .from(builderStartPosition)
            .to(builderTargetPosition)
            .andBuild(opponentPosition)
        )
      }
    }

    @Test
    fun `can't build if there is no building block remaining`() {
      val g = newGameWithSequentiallyPlacedBuilders(
        blocks = BlocksData(
          Height(1) to 1,
          Height(2) to 1
        )
      )

      // can build once as there is exactly 1 block available

      g.play(
        DSL.player(0).building.move().from(0, 0).to(0, 1).andBuild(0, 0)
      )

      // can't build a block for height 1 as there is none remaining

      val builderStartPosition = P(1, 0)
      val builderTargetPosition = P(1, 1)
      val buildingPosition = P(1, 0)

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(1).building
            .move()
            .from(builderStartPosition)
            .to(builderTargetPosition)
            .andBuild(buildingPosition)
        )
      }
    }

    @Test
    fun `can't build more than 1 tile away from the moved builder`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(0).building
            .move()
            .from(P(0, 0))
            .to(P(0, 1))
            .andBuild(P(4, 4))
        )
      }
    }
  }

  @Nested
  inner class Seal {

    @Test
    fun `can build a seal`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      val seal = P(0, 0)

      g.play(
        DSL.player(0).building
          .move()
          .from(0, 0)
          .to(0, 1)
          .andSeal(seal)
      )

      assertThat(g.state.seals[seal]).isTrue()
    }

    @Test
    fun `can't move to a tile with a seal`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      val seal = P(0, 0)

      val state0 = g.state

      g.play(
        DSL.player(0).building
          .move()
          .from(0, 0)
          .to(0, 1)
          .andSeal(seal)
      )

      GameStateAssert.assertThat(g.state).isEqualTo(
        state0.copy(
          players = listOf(Player(1), Player(0)),
          seals = Matrix.from(
            """
            |1 0 0 0 0
            |0 0 0 0 0
            |0 0 0 0 0
            |0 0 0 0 0
            |0 0 0 0 0
          """.trimMargin(),
          ) { it == "1" },
          builders = Matrix.from(
            """
            |. 1 0 1 .
            |0 . . . .
            |. . . . .
            |. . . . .
            |. . . . .
          """.trimMargin(),
          ) { if (it == ".") null else it.toInt() },
          blocks = state0.blocks.removeSeal()
        )
      )

      val state1 = g.state

      val start = P(1, 0)

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(1).building
            .move()
            .from(start)
            .to(seal)
            .andBuild(start)
        )
      }

      GameStateAssert.assertThat(g.state)
        .describedAs("Check no state change")
        .isEqualTo(state1)
    }

    @Test
    fun `can't build under a seal`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      val start = P(0, 0)
      val target = P(0, 1)
      val building = P(0, 0)

      (g as GameImpl).backdoor.forceState(g.state.seal(building))

      val state0 = g.state

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(0).building
            .move()
            .from(start)
            .to(target)
            .andBuild(building)
        )
      }

      assertThat(g.state)
        .describedAs("Check no state change")
        .isEqualTo(state0)
    }

    @Test
    fun `can't seal on top of a seal`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      val start = P(0, 0)
      val target = P(0, 1)
      val seal = P(0, 0)

      (g as GameImpl).backdoor.forceState(g.state.seal(seal))

      val state0 = g.state

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(0).building
            .move()
            .from(start)
            .to(target)
            .andSeal(seal)
        )
      }

      assertThat(g.state)
        .describedAs("Check no state change")
        .isEqualTo(state0)
    }

    @Test
    fun `can't seal more than 1 tiles away from the target position`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(0).building.move().from(0, 0).to(1, 1).andSeal(3, 3)
        )
      }
    }
  }

  @Nested
  inner class Finished {

    // TODO: should the eliminated player keep their builders on the board?
    @Test
    fun `when a player gives up, he can't play anymore`() {
      val g = newGameWithSequentiallyPlacedBuilders(
        playerCount = 3,
        buildersPerPlayer = 2,
        width = 100
      )

      assertThat(g.turn).isEqualTo(6)
      assertThat(g.state.currentPlayer).isEqualTo(0)

      g.play(
        DSL.player(0).building.giveUp()
      )
      assertThat(g.turn).isEqualTo(7)
      assertThat(g.state.currentPlayer).isEqualTo(1)

      g.play(
        DSL.player(1).building.move().from(1, 0).to(1, 1).andBuild(1, 0)
      )

      assertThat(g.turn).isEqualTo(8)
      assertThat(g.state.currentPlayer).isEqualTo(2)

      g.play(
        DSL.player(2).building.move().from(2, 0).to(2, 1).andBuild(2, 0)
      )

      // now the player 0 doesn't play because it gave up

      assertThat(g.turn).isEqualTo(9)
      assertThat(g.state.currentPlayer).isEqualTo(1)
    }

    @Test
    fun `when there is only 1 player remaining, the game is finished`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      assertThat(g.state.isFinished()).isFalse

      g.play(
        DSL.player(0).building.giveUp()
      )

      assertThat(g.state.isFinished()).isTrue
      assertThat(g.state.phase).isEqualTo(Phase.FINISHED)
    }

    @Test
    fun `when a player reaches the max height, they win`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      val start = P(0, 0)
      val target = P(1, 1)
      (g as GameImpl).backdoor.forceState(
        g.state
          .height(start, 2)
          .height(target, 3)
      )

      val state0 = g.state

      g.play(
        DSL.player(0).building.move().from(start).to(target).andWin()
      )

      GameStateAssert.assertThat(g.state).isEqualTo(
        state0.copy(
          players = listOf(Player(0), Player(1, false)),
          builders = state0.builders.copyAndSwap(start, target)
        )
      )
    }

    @Test
    fun `asking nicely doesn't grant victory`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      val start = P(0, 0)
      val target = P(1, 1)

      assertThrows<GameRuleViolationException> {
        g.play(
          DSL.player(0).building.move().from(start).to(target).andWin()
        )
      }
    }
  }
}
