package net.lab0.skyscrapers.server

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.ErrorMessage
import net.lab0.skyscrapers.engine.GameFactoryImpl
import net.lab0.skyscrapers.engine.api.Game
import net.lab0.skyscrapers.server.JoiningError.GameNotFound

class ServiceImpl(
  val games: MutableMap<GameName, Game>,
) : Service {
  companion object {
    fun new(initialGames: Map<GameName, Game> = mutableMapOf()) =
      ServiceImpl(initialGames.toMutableMap())
  }

  private val playersInGame =
    mutableMapOf<GameName, MutableList<PlayerAndToken>>()

  override fun getGame(name: GameName): Either<ErrorMessage, Game> =
    games[name]?.let { Right(it) }
      ?: Left(
        ErrorMessage(
          "No game named 'missing'. " +
              "There ${if(games.size <= 1) "is" else "are"} ${games.size} available game" +
              when (games.size) {
                0 -> "."
                1 -> ". " + games.keys.first().value
                else -> "s. " + games.keys.joinToString { it.value }
              }
        )
      )

  override fun createGame(name: GameName): Game =
    GameFactoryImpl().new().also { games[name] = it }

  override fun join(gameName: GameName): Either<JoiningError, PlayerAndToken> =
    getGame(gameName)
      .mapLeft(::GameNotFound)
      .map { game ->
        val existingPlayers =
          playersInGame.computeIfAbsent(gameName) { mutableListOf() }

        if (game.state.players.size == existingPlayers.size) {
          return Left(JoiningError.GameIsFull)
        }

        val playerAndToken = PlayerAndToken(
          existingPlayers.size,
          AccessToken.random()
        )

        existingPlayers.add(playerAndToken)

        playerAndToken
      }

  override fun getGameNames(): Set<GameName> =
    games.keys.toSet()

  override fun canParticipate(game: GameName, token: AccessToken): Boolean {
    val players = playersInGame[game]
    return players?.map { it.token.value }
      ?.contains(token.value) == true
  }

  override fun getPlayerId(game: GameName, token: AccessToken) =
    playersInGame[game]
      ?.first { it.token == token }
      ?.id
}
