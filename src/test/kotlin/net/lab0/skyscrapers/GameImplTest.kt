package net.lab0.skyscrapers

import net.lab0.skyscrapers.actions.DSL
import net.lab0.skyscrapers.exception.*
import net.lab0.skyscrapers.state.GameStateData
import net.lab0.skyscrapers.state.Matrix
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import net.lab0.skyscrapers.Position as P

internal class GameImplTest {

  companion object {
    fun newGameWithSequentiallyPlacedBuilders(
      width: Int = Defaults.WIDTH,
      height: Int = Defaults.HEIGHT,
      playerCount: Int = Defaults.PLAYER_COUNT,
      buildersPerPlayer: Int = Defaults.BUILDERS_PER_PLAYER,
      blocks: Map<Height, Int> = Defaults.BLOCKS,
    ) = Game.new(width, height, playerCount, buildersPerPlayer, blocks).also {
      (0 until playerCount * buildersPerPlayer).forEach { turn ->
        val player = turn % playerCount
        val x = turn % width
        val y = turn / height
        it.play(
          DSL.player(player).placement.addBuilder(P(x, y))
        )
      }
    }
  }

  @Nested
  inner class State {
    @Test
    fun `newGameWithSequentiallyPlacedBuilders generates this specific game by default`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      assertThat(g.getState()).isEqualTo(
        GameStateData.from(
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
          """.trimIndent()
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

      return (0 until width).flatMap { x ->
        (0 until height).map { y ->
          DynamicTest.dynamicTest("g[$x,$y] == 0") {
            assertThat(g.getHeight(x, y)).isEqualTo(Height(0))
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

    @Test
    fun `some blocks to build must be present`() {
      assertThat(
        assertThrows<InvalidBlocksConfiguration> {
          Game.new(blocks = mapOf())
        }
      ).hasMessage("There must be at least 1 block for the game to make sense")

      assertThat(
        assertThrows<InvalidBlocksConfiguration> {
          Game.new(
            blocks = mapOf(Height(1) to 0, Height(2) to 0)
          )
        }
      ).hasMessage("There must be at least 1 block for the game to make sense")
    }

    @Test
    fun `there must be no gap the the blocks heights`() {
      assertThat(
        assertThrows<InvalidBlocksConfiguration> {
          Game.new(
            blocks = mapOf(Height(1) to 10, Height(3) to 5)
          )
        }
      ).hasMessage("There must no gap in the blocks heights")
    }

    @Test
    fun `there must be more block of lower height than higher height`() {
      assertThat(
        assertThrows<InvalidBlocksConfiguration> {
          Game.new(
            blocks = mapOf(Height(1) to 1, Height(2) to 99)
          )
        }
      ).hasMessage("The lower blocks must be in larger quantity than the higher blocks")
    }

    @Test
    fun `each block must be proposed at least once`() {
      assertThat(
        assertThrows<InvalidBlocksConfiguration> {
          Game.new(
            blocks = mapOf(Height(1) to 1, Height(2) to 0, Height(3) to 0)
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
      assertThat(g.phase).isEqualTo(Phase.PLACEMENT)
    }

    @Test
    fun `the players can place their builder`() {
      val g: Game = Game.new()
      val player = 0

      g.play(
        DSL.player(player).placement.addBuilder(P(1, 2))
      )

      assertThat(g.getBuilders(player)).isEqualTo(listOf(P(1, 2)))
    }

    @Test
    fun `players must add their builders in alternating turns`() {
      val g: Game = Game.new()

      g.play(
        DSL.player(0).placement.addBuilder(0, 0)
      )

      assertThrows<WrongPlayerTurn> {
        g.play(
          DSL.player(0).placement.addBuilder(1, 1)
        )
      }

      g.play(
        DSL.player(1).placement.addBuilder(1, 1)
      )

      assertThrows<WrongPlayerTurn> {
        g.play(
          DSL.player(1).placement.addBuilder(2, 2)
        )
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

      g.play(
        DSL.player(0).placement.addBuilder(0, 0)
      )

      assertThat(g.turn).isEqualTo(1)
      assertThat(g.currentPlayer).isEqualTo(1)

      g.play(
        DSL.player(1).placement.addBuilder(1, 1)
      )

      assertThat(g.turn).isEqualTo(2)
      assertThat(g.currentPlayer).isEqualTo(0)
    }

    @Test
    fun `can't place a builder on top of another builder`() {
      val g = Game.new()

      g.play(
        DSL.player(0).placement.addBuilder(0, 0)
      )

      assertThrows<CellUsedByAnotherBuilder> {
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
    fun `the player can't give up in the placement phase`() {
      val g = Game.new()

      assertThrows<CantGiveUpInThePlacementPhase> {
        g.play(
          DSL.player(0).building.giveUp()
        )
      }
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
        DSL.player(1).placement.addBuilder(5, 5)
      )

      // can't move the first builder before placing them all
      assertThrows<IllegalMove> {
        g.play(
          DSL.player(0).building.move().from(0, 0).to(0, 1).andBuild(0, 0)
        )
      }
    }
  }

  @Nested
  inner class Movement {

    // TODO: should the eliminated player keep their builders on the board?
    @Test
    fun `when a player gives up, he can't play anymore`() {
      val g = newGameWithSequentiallyPlacedBuilders(
        playerCount = 3,
        buildersPerPlayer = 2,
        width = 100
      )

      assertThat(g.turn).isEqualTo(6)
      assertThat(g.currentPlayer).isEqualTo(0)

      g.play(
        DSL.player(0).building.giveUp()
      )
      assertThat(g.turn).isEqualTo(7)
      assertThat(g.currentPlayer).isEqualTo(1)

      g.play(
        DSL.player(1).building.move().from(1, 0).to(1, 1).andBuild(1, 0)
      )

      assertThat(g.turn).isEqualTo(8)
      assertThat(g.currentPlayer).isEqualTo(2)

      g.play(
        DSL.player(2).building.move().from(2, 0).to(2, 1).andBuild(2, 0)
      )

      // now the player 0 doesn't play because it gave up

      assertThat(g.turn).isEqualTo(9)
      assertThat(g.currentPlayer).isEqualTo(1)
    }

    @Test
    fun `the player can move a builder`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      g.play(
        DSL.player(0).building.move().from(0, 0).to(0, 1).andBuild(0, 0)
      )

      assertThat(g.getBuilders(0))
        .contains(P(0, 1), P(2, 0))

    }

    @Test
    fun `the movement range of a builder is limited to the 8 cells around its initial position`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      assertThrows<IllegalMove> {
        g.play(
          DSL.player(0).building.move().from(0, 0).to(0, 2).andBuild(0, 0)
        )
      }
    }

    @Test
    fun `the player must move, it can't stay at the same place`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      assertThrows<IllegalMove> {
        g.play(
          DSL.player(0).building.move().from(0, 0).to(0, 0).andBuild(0, 0)
        )
      }
    }

    @Test
    fun `can't move another player's builder`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      assertThrows<IllegalMove> {
        g.play(
          DSL.player(0).building.move().from(1, 0).to(1, 1).andBuild(1, 0)
        )
      }
    }

    @Test
    fun `can't move a builder that doesn't exist`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      assertThrows<IllegalMove> {
        g.play(
          DSL.player(0).building.move().from(5, 5).to(4, 4).andBuild(5, 5)
        )
      }
    }

    @Test
    fun `can't play as a player that doesn't exist`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      assertThrows<WrongPlayerTurn> {
        g.play(
          DSL.player(99).building.move().from(0, 0).to(0, 1).andBuild(0, 0)
        )
      }
    }

    @Test
    fun `can't move on top of another player's builder`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      assertThrows<IllegalMove> {
        g.play(
          DSL.player(0).building.move().from(0, 0).to(1, 0).andBuild(0, 0)
        )
      }
    }

    @Test
    fun `can't move out of bounds`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      assertThrows<IllegalMove> {
        g.play(
          DSL.player(0).building.move().from(0, 0).to(-1, -1).andBuild(0, 0)
        )
      }
    }

    @Test
    fun `can only move up 1 step at a time`() {
      val g = newGameWithSequentiallyPlacedBuilders() as GameImpl

      val start = P(0, 0)
      val end = P(1, 1)
      val build = P(0, 0)

      g.backdoor.setHeight(end, 2) // too high

      assertThat(
        assertThrows<IllegalMove> {
          g.play(
            DSL.player(0).building.move().from(start).to(end).andBuild(build)
          )
        }
      ).isEqualTo(
        IllegalMove(
          start,
          end,
          "can't move more than 1 level each step. You tried to move up by 2 levels"
        )
      )
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

      assertThat(g.getHeight(buildPosition)).isEqualTo(Height(1))

      g.play(
        DSL.player(1).building
          .move()
          .from(1, 0)
          .to(1, 1)
          .andBuild(buildPosition)
      )

      // building stacks
      assertThat(g.getHeight(buildPosition)).isEqualTo(Height(2))
    }

    @Test
    fun `can't build outside of the board`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      val builderStartPosition = P(0, 0)
      val builderTargetPosition = P(0, 1)
      val buildingPosition = P(-1, 1)

      assertThat(
        assertThrows<IllegalBuilding> {
          g.play(
            DSL.player(0).building
              .move()
              .from(builderStartPosition)
              .to(builderTargetPosition)
              .andBuild(buildingPosition)
          )
        }
      ).isEqualTo(
        IllegalBuilding(
          builderTargetPosition,
          buildingPosition,
          "outside of the board"
        )
      )

      // the player's movement didn't occur either
      assertThat(g.hasBuilder(builderStartPosition)).isTrue()
      assertThat(g.hasBuilder(builderTargetPosition)).isFalse()
    }

    @Test
    fun `can't build where there is a builder on that tile`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      val builderStartPosition = P(0, 0)
      val builderTargetPosition = P(0, 1)
      val opponentPosition = P(1, 0)

      assertThat(
        assertThrows<IllegalBuilding> {
          g.play(
            DSL.player(0).building
              .move()
              .from(builderStartPosition)
              .to(builderTargetPosition)
              .andBuild(opponentPosition)
          )
        }
      ).isEqualTo(
        IllegalBuilding(
          builderTargetPosition,
          opponentPosition,
          "builder is present at build location"
        )
      )
    }

    @Test
    fun `can't build if there is no building block remaining`() {
      val g = newGameWithSequentiallyPlacedBuilders(
        blocks = mapOf(
          Height(1) to 1,
          Height(2) to 1
        )
      )

      // can play once as the is exactly 1 block available

      g.play(
        DSL.player(0).building.move().from(0, 0).to(0, 1).andBuild(0, 0)
      )

      // can't play a block for height 1 as there is none remaining

      val builderStartPosition = P(1, 0)
      val builderTargetPosition = P(1, 1)
      val buildingPosition = P(1, 0)

      assertThat(
        assertThrows<IllegalBuilding> {
          g.play(
            DSL.player(1).building
              .move()
              .from(builderStartPosition)
              .to(builderTargetPosition)
              .andBuild(buildingPosition)
          )
        }
      ).isEqualTo(
        IllegalBuilding(
          builderTargetPosition,
          buildingPosition,
          "no block of height 1 remaining"
        )
      )
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
          .andBuildSeal(seal)
      )

      assertThat(g.hasSeal(seal)).isTrue()
    }

    @Test
    fun `can't move to tile with a seal`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      val start = P(1, 0)
      val seal = P(0, 0)

      val state0 = g.getState()

      g.play(
        DSL.player(0).building
          .move()
          .from(0, 0)
          .to(0, 1)
          .andBuildSeal(seal)
      )

      assertThat(g.getState()).isEqualTo(
        state0.copy(
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
          ) { if(it == ".") null else it.toInt() }
        )
      )

      assertThat(
        assertThrows<IllegalMove> {
          g.play(
            DSL.player(1).building
              .move()
              .from(start)
              .to(seal)
              .andBuild(start)
          )
        }
      ).isEqualTo(
        IllegalMove(start, seal, "the position [0, 0] is sealed")
      )
    }

    // TODO: can't build under a seal
    // TODO: can't build a seal on top of a seal
  }

  @Nested
  inner class Finished {

    @Test
    fun `when there is only 1 player remaining, the game is finished`() {
      val g = newGameWithSequentiallyPlacedBuilders()

      assertThat(g.isFinished()).isFalse

      g.play(
        DSL.player(0).building.giveUp()
      )

      assertThat(g.isFinished()).isTrue
    }

    // TODO reach max level
  }
}
