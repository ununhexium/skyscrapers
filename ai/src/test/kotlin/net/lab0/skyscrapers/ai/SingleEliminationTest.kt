package net.lab0.skyscrapers.ai

import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import net.lab0.skyscrapers.engine.GameFactoryImpl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SingleEliminationTest {
  @Test
  fun `throws IllegalArgumentException if there is not at least 1 participant`() {
    assertThrows<IllegalArgumentException> {
      SingleElimination(200) {
        GameFactoryImpl().new()
      }.compete(emptyList())
    }
  }

  @Test
  fun `when there is only one participant, it wins without the need to play`() {
    // given
    val winner = SingleElimination(200) {
      GameFactoryImpl().new()
    }.compete(listOf(GiveUp))

    // then
    winner.first shouldBe GiveUp
    winner.second shouldBe emptyList()
  }

  @Test
  fun `make AIs play against each other`() {
    val participants = Array(5) { RandomAi() }.toList()
    val result = SingleElimination(200) {
      GameFactoryImpl().new()
    }.compete(participants)

    val winner = result.first
    winner should beInstanceOf<RandomAi>()
    result.second.toSet() shouldBe participants.filter { it !== winner }.toSet()
  }
}
