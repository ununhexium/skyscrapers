package net.lab0.skyscrapers.ai

import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import net.lab0.skyscrapers.engine.GameFactoryImpl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AisTournament {
  @Test
  fun `throws IllegalArgumentException if there is not at least 1 participant`() {
    assertThrows<IllegalArgumentException> {
      SingleElimination(200) {
        GameFactoryImpl().new()
      }.compete(emptySequence())
    }
  }

  @Test
  fun `when there is only one participant, it wins without the need to play`() {
    // given
    val winner = SingleElimination(200) {
      GameFactoryImpl().new()
    }.compete(sequenceOf(GiveUp))

    winner shouldBe GiveUp

    // when

    // then
  }

  @Test
  fun `make AIs play against each other`() {
    val winner = SingleElimination(200) {
      GameFactoryImpl().new()
    }.compete(sequence { repeat(5) { yield(RandomAi()) } })

    winner should beInstanceOf<RandomAi>()
  }
}
