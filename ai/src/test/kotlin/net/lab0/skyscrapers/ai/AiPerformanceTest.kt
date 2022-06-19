package net.lab0.skyscrapers.ai

import net.lab0.skyscrapers.engine.GameFactoryImpl
import org.junit.jupiter.api.Test

/**
 * Make AIs play a tournament to see which one is the best.
 */
class AiPerformanceTest {
  @Test
  fun `default settings`() {
    // given
    val tournament = SingleElimination(100) {
      GameFactoryImpl().new()
    }

    val ais = listOf(
      RandomAi(),
      SequentialAi(),
    )

    // when
    val (winner, others) = tournament.compete(
      ais + ais + ais + ais // each AI has 4 attempts
    )

    // then
    println("Winner = ${winner.name}")
    println("Others = " + others.joinToString { it.name })
  }
}
