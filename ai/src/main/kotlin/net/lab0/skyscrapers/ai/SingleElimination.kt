package net.lab0.skyscrapers.ai

import mu.KLogger
import mu.KotlinLogging
import net.lab0.skyscrapers.engine.api.Game
import kotlin.random.Random

/**
 * @return The winner or `null` if it's a draw.
 *
 * @throws IllegalArgumentException if there is not at least 1 participant.
 */
class SingleElimination(
  val maxTurn: Int,
  val gameFactory: () -> Game,
) : Tournament<Ai> {

  val log = klassLogger()

  private fun klassLogger(): KLogger =
    KotlinLogging.logger(this::class.qualifiedName!!)

  override fun compete(participants: List<Ai>): Pair<Ai, MutableList<Ai>> {
    val random = Random(0) // not random, just shuffling
    val eliminated = mutableListOf<Ai>()
    var remaining = participants.shuffled(random)
    if (remaining.isEmpty()) throw IllegalArgumentException("There must be at least 1 participant.")

    while (remaining.size >= 2) {
      val pairs = remaining.chunked(2)
      remaining = pairs.map { maybePair ->
        if (maybePair.size == 2) {
          val (a, b) = maybePair
          val winner = bestOf1(a, b) ?: if (Random.nextBoolean()) a else b
          log.info { "${a.name} VS ${b.name}: ${winner.name} wins" }
          eliminated.add(if(a === winner) b else a)
          winner
        } else maybePair.first()
      }
    }

    return remaining.first() to eliminated
  }

  private fun bestOf1(a: Ai, b: Ai): Ai? {
    val game = gameFactory()

    while (!game.state.isFinished() && game.turn < maxTurn) {
      game.play(a.think(game.state.currentPlayer, game.state, game.ruleBook))
      if (game.state.isFinished()) break
      game.play(b.think(game.state.currentPlayer, game.state, game.ruleBook))
    }

    return game.state.winner?.let {
      if (it == 0) a else b
    }
  }
}
