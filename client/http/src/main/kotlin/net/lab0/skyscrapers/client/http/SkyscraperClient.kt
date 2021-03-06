package net.lab0.skyscrapers.client.http

import arrow.core.Either
import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.ConnectionResponse
import net.lab0.skyscrapers.api.dto.GameResponse
import net.lab0.skyscrapers.api.dto.HistoryResponseDTO
import net.lab0.skyscrapers.api.dto.StatusResponse
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.Errors
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.Position
import org.http4k.core.Status

interface SkyscraperClient {

  // TODO: return an error message instead of the server's status.
  fun status(): Either<Status, StatusResponse>

  fun state(name: GameName): Either<Errors, GameState>

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

  fun place(
    name: GameName,
    token: AccessToken,
    position: Position,
  ): Either<ClientError, GameState>

  fun build(
    name: GameName,
    token: AccessToken,
    start: Position,
    target: Position,
    build: Position,
  ): Either<ClientError, GameState>

  fun seal(
    name: GameName,
    token: AccessToken,
    start: Position,
    target: Position,
    seal: Position,
  ): Either<ClientError, GameState>

  fun win(
    name: GameName,
    token: AccessToken,
    start: Position,
    target: Position,
  ): Either<ClientError, GameState>

  fun history(name: GameName): Either<ClientError, List<GameState>>
}
