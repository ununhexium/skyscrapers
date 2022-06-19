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

    val participants = (1..4).fold(listOf<Ai>()) { acc, e -> acc + ais }

    // when
    val (winner, others) = tournament.compete(participants)

    // then
    println("Winner = ${winner.name}")
    println("Others = " + others.joinToString { it.name })
  }
}
