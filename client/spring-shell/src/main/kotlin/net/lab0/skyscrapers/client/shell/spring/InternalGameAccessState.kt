package net.lab0.skyscrapers.client.shell.spring

import arrow.core.Either
import arrow.core.Either.Left
import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.Errors
import net.lab0.skyscrapers.client.http.SkyscraperClient
import net.lab0.skyscrapers.engine.Defaults
import net.lab0.skyscrapers.engine.utils.StateBrowser

data class InternalGameAccessState(
  val baseUrl: BaseUrl? = null,
  val client: SkyscraperClient? = null,
  val playerId: Int? = null,
  val currentGame: GameName? = null,
  val accessToken: AccessToken? = null,
) {
  val inGame
    get() = isConnected() && currentGame != null && accessToken != null

  fun isConnected(): Boolean =
    client != null

  fun <T> useClient(f: (SkyscraperClient) -> T?): T? =
    if(isConnected()) f(client!!) else null

  fun <T> useGameClient(f: (InGameClient) -> T?): T? =
    useClient { client ->
      if(inGame) f(InGameClient(client, currentGame!!, accessToken!!)) else null
    }

  fun <T> useStateBrowser(f: (InGameStateBrowser) -> T): Either<Errors, T> =
    useGameClient { client ->
      client.state().map { gameState ->
        f(InGameStateBrowser(playerId!!, StateBrowser(gameState, Defaults.RULE_BOOK)))
      }
    } ?: Left(listOf("Not in a game"))
}
