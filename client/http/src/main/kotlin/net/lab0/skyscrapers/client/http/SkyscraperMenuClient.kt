package net.lab0.skyscrapers.client.http

import arrow.core.Either
import net.lab0.skyscrapers.server.dto.GameResponse
import net.lab0.skyscrapers.server.value.GameName

interface SkyscraperMenuClient {
  /**
   * Lists the games available on the server
   */
  fun listGames(): List<GameName>

  /**
   * Create a game on the server.
   */
  fun create(name: GameName): Either<List<String>, GameResponse>

  /**
   * Join a game
   */
  fun join(name: GameName): SkyscraperGameClient
}
