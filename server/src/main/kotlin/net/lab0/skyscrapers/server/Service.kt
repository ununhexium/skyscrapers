package net.lab0.skyscrapers.server

import arrow.core.Either
import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.ErrorMessage
import net.lab0.skyscrapers.engine.api.Game

// TODO: this should never return a game
interface Service {
  fun getGame(name: GameName): Either<ErrorMessage, Game>
  fun createGame(name: GameName): Game
  fun join(gameName: GameName): Either<JoiningError, PlayerAndToken>
  fun getGameNames(): Set<GameName>

  /**
   * Check if a player can play on a game.
   * Doesn't check if it can play on this specific turn,
   * only if it has access rights and is an authorized player.
   *
   * @return `true` if the player authenticated with the token can play on the given game.
   */
  fun canParticipate(game: GameName, token: AccessToken): Boolean

  fun getPlayerId(game: GameName, token: AccessToken): Int?
}
