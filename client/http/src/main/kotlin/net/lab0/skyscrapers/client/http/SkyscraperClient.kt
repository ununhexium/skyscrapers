package net.lab0.skyscrapers.client.http

import arrow.core.Either
import net.lab0.skyscrapers.engine.api.GameState
import net.lab0.skyscrapers.engine.api.TurnType
import net.lab0.skyscrapers.server.dto.ConnectionResponse
import net.lab0.skyscrapers.server.dto.GameResponse
import net.lab0.skyscrapers.server.dto.StatusResponse
import net.lab0.skyscrapers.server.value.GameName
import org.http4k.core.Status

interface SkyscraperClient {
  /**
   * Choose which server to play on
   *
   * @return a menu client if successful is successful, `null` otherwise.
   */
  fun status(): Either<Status, StatusResponse>

  fun state(name: GameName): Either<Errors, GameState>

  fun play(turn: TurnType): Either<Errors, GameState>

  /**
   * Lists the games available on the server
   */
  fun listGames(): List<GameName>

  /**
   * Create a game on the server.
   */
  fun create(name: GameName): Either<Errors, GameResponse>

  /**
   * Join a game
   */
  fun join(name: GameName): Either<Errors, ConnectionResponse>
}
