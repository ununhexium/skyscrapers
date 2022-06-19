package net.lab0.skyscrapers.ai

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import net.lab0.skyscrapers.api.structure.Height
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.api.structure.VictoryType
import net.lab0.skyscrapers.engine.Defaults
import net.lab0.skyscrapers.engine.GameFactoryImpl
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class AiValidityTest {
  data class Subject(val name: String, val factory: () -> Ai)

  val subjects = listOf(
    Subject("random") { RandomAi() },
    Subject("sequential") { SequentialAi() },
  )

  @Disabled("Not stable, sometimes it works, sometimes it doesn't.")
  @TestFactory
  fun `the AI plays until the game is blocked`(): Iterable<DynamicTest> {
    return subjects.map { subject ->
      DynamicTest.dynamicTest(subject.name) {
        // given
        val ai0 = subject.factory()
        val ai1 = subject.factory()
        val game = GameFactoryImpl().new()

        // when
        val limit = 200
        var steps = 0
        while (steps++ < limit) {
          if (game.state.isFinished()) break

          game.play(ai0.think(0, game.state, Defaults.RULE_BOOK))

          if (game.state.isFinished()) break

          game.play(ai1.think(1, game.state, Defaults.RULE_BOOK))
        }

        // then
        game.state.isFinished() shouldBe true
        game.state.victoryType shouldNotBe null
      }
    }
  }
}