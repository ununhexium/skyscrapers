package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.logic.api.Game
import net.lab0.skyscrapers.server.value.GameName

interface Service {
  fun getGame(name: GameName): Game?
  fun createGame(name: GameName): Game
  fun connect(gameName: GameName): PlayerAndToken
}