package net.lab0.skyscrapers.server

import arrow.core.Either
import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.ErrorMessage
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.api.Game

// TODO: this should never return a game
interface Service {
  @Deprecated("Use getGameState() or another direct access method")
  fun getGame(name: GameName): Either<ErrorMessage, Game>

  fun createGameIfItDoesntExist(name: GameName): Either<ErrorMessage, GameState>
  fun getGameHistory(name: GameName): Either<ErrorMessage, List<GameState>>
  fun getGameNames(): Set<GameName>
  fun getGameState(name: GameName): Either<ErrorMessage, GameState>
  fun join(gameName: GameName): Either<JoiningError, PlayerAndToken>
  fun playGame(name:GameName, turn: TurnType):
      Either<ErrorMessage, GameState>

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
