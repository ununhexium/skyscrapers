package net.lab0.skyscrapers.client.http

import net.lab0.skyscrapers.server.value.GameName

interface SkyscraperMenuClient {
  /**
   * Lists the games available on the server
   */
  fun listGames(): List<GameName>

  /**
   * Join a game
   */
  fun join(gameName: GameName): SkyscraperGameClient
}
