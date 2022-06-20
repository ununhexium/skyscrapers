package net.lab0.skyscrapers.client.shell.spring

import mu.KotlinLogging
import net.lab0.skyscrapers.ai.Ai
import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.client.http.SkyscraperClient
import net.lab0.skyscrapers.engine.Defaults

class AiRunnable(
  val client: SkyscraperClient,
  val game: GameName,
  val player: Int,
  val token: AccessToken,
  val ai: Ai
) : Runnable {

  val log = KotlinLogging.logger(this::class.qualifiedName!!)

  override fun run() {
    var finished = false
    var stateFailed = 0

    log.info { "Started ai ${ai.name}" }

    while (!finished) {
      // don't have time to make the WS work now. Going to use polling.
      Thread.sleep(1000)

      client.state(game).map { state ->
        if (state.isFinished()) {
          finished = true
        }
        else if (state.currentPlayer == player) {
          val decision = ai.think(player, state, Defaults.RULE_BOOK)
          when (decision) {
            is TurnType.GiveUpTurn -> TODO("Implement giving up")
            is TurnType.MoveTurn.BuildTurn -> client.build(
              game,
              token,
              decision.start,
              decision.target,
              decision.build,
            )
            is TurnType.MoveTurn.SealTurn -> client.seal(
              game,
              token,
              decision.start,
              decision.target,
              decision.seal,
            )
            is TurnType.MoveTurn.WinTurn -> client.win(
              game,
              token,
              decision.start,
              decision.target,
            )
            is TurnType.PlacementTurn -> client.place(
              game,
              token,
              decision.position
            )
          }

          log.info { "Played $decision" }
        }
      }.mapLeft {
        stateFailed++
        if (stateFailed > 3) finished = true
        log.warn { "Failed to get the state. Counter=$stateFailed" }
      }
    }
  }
}
