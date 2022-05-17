package net.lab0.skyscrapers.engine.assertj

import net.lab0.skyscrapers.engine.api.GameState
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions.assertThat as at

class GameStateAssert(actual: GameState) // assertion methods described later
  : AbstractAssert<GameStateAssert, GameState>(
  actual,
  GameStateAssert::class.java
) {
  companion object {
    fun assertThat(actual: GameState) =
      GameStateAssert(actual)
  }

  fun isEqualTo(expected: GameState) {
    at(actual.players).describedAs("Players").isEqualTo(expected.players)
    at(actual.maxBuildersPerPlayer).describedAs("Max builders").isEqualTo(expected.maxBuildersPerPlayer)
    at(actual.blocks).describedAs("Blocks").isEqualTo(expected.blocks)
    at(actual.buildings).describedAs("Buildings").isEqualTo(expected.buildings)
    at(actual.seals).describedAs("Seals").isEqualTo(expected.seals)
    at(actual.builders).describedAs("Builders").isEqualTo(expected.builders)
  }
}

