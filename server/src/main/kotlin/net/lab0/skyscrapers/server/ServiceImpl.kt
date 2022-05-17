package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.engine.api.Game
import net.lab0.skyscrapers.server.exception.GameFullException
import net.lab0.skyscrapers.server.exception.GameNotFound
import net.lab0.skyscrapers.server.value.GameName
import java.util.*

class ServiceImpl(val games: MutableMap<GameName, Game>) : Service {
  companion object {
    fun new(initialGames: Map<GameName, Game> = mutableMapOf()) =
      ServiceImpl(initialGames.toMutableMap())
  }

  private val playersInGame = mutableMapOf<GameName, MutableList<PlayerAndToken>>()

  override fun getGame(name: GameName): Game? =
    games[name]

  override fun createGame(name: GameName): Game =
    Game.new().also { games[name] = it }

  override fun connect(gameName: GameName): PlayerAndToken {
    val game = getGame(gameName)
      ?: throw GameNotFound(gameName)

    val existingPlayers =
      playersInGame.computeIfAbsent(gameName) { mutableListOf() }

    if (game.state.players.size == existingPlayers.size) {
      throw GameFullException(gameName)
    }

    val p = PlayerAndToken(
      existingPlayers.size,
      UUID.randomUUID().toString()
    )

    existingPlayers.add(p)

    return p
  }
}