package net.lab0.skyscrapers.client.shell.spring

import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.client.http.SkyscraperClient

data class InternalGameAccessState(
  val baseUrl: BaseUrl? = null,
  val client: SkyscraperClient? = null,
  val currentGame: GameName? = null,
  val accessToken: AccessToken? = null,
) {
  val inGame
    get() = isConnected() && currentGame != null

  fun isConnected(): Boolean =
    client != null

  fun <T> useClient(f: (SkyscraperClient) -> T?): T? =
    if(isConnected()) f(client!!) else null
}
