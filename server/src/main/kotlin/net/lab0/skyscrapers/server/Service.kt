package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.engine.api.Game
import net.lab0.skyscrapers.api.dto.value.GameName

interface Service {
  fun getGame(name: GameName): Game?
  fun createGame(name: GameName): Game
  fun join(gameName: GameName): PlayerAndToken
  fun getGameNames(): Set<GameName>
  fun canPlay(game: GameName, token: String): Boolean
}
