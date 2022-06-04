package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.engine.GameFactoryImpl
import net.lab0.skyscrapers.engine.api.Game
import net.lab0.skyscrapers.server.exception.GameFullException
import net.lab0.skyscrapers.server.exception.GameNotFound
import net.lab0.skyscrapers.api.dto.value.GameName
import java.util.*

class ServiceImpl(
  val games: MutableMap<GameName, Game>,
) : Service {
  companion object {
    fun new(initialGames: Map<GameName, Game> = mutableMapOf()) =
      ServiceImpl(initialGames.toMutableMap())
  }

  private val playersInGame =
    mutableMapOf<String, MutableList<PlayerAndToken>>()

  override fun getGame(name: GameName): Game? =
    games[name]

  override fun createGame(name: GameName): Game =
    GameFactoryImpl().new().also { games[name] = it }

  // TODO: connecting to a game is joining a game. Rename to "join"
  override fun join(gameName: GameName): PlayerAndToken {
    val game = getGame(gameName)
      ?: throw GameNotFound(gameName)

    val existingPlayers =
      playersInGame.computeIfAbsent(gameName.value) { mutableListOf() }

    if (game.state.players.size == existingPlayers.size) {
      throw GameFullException(gameName)
    }

    val p = PlayerAndToken(
      existingPlayers.size,
      AccessToken.random()
    )

    existingPlayers.add(p)

    return p
  }

  override fun getGameNames(): Set<GameName> =
    games.keys.toSet()

  override fun canParticipate(game: GameName, token: AccessToken): Boolean {
    val players = playersInGame[game.value]
    return players?.map { it.token.value }
      ?.contains(token.value) == true
  }

  override fun getPlayerId(game: GameName, token: AccessToken) =
    playersInGame[game.value]
      ?.first { it.token == token }
      ?.id
}
