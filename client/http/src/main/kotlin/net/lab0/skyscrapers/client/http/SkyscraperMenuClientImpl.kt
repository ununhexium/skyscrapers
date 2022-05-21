package net.lab0.skyscrapers.client.http

import net.lab0.skyscrapers.server.value.GameName
import org.http4k.core.Uri

class SkyscraperMenuClientImpl(val apiUrl: Uri) : SkyscraperMenuClient {

  override fun listGames(): List<GameName> {
    TODO("Not yet implemented")
  }

  override fun join(gameName: GameName): SkyscraperGameClient {
    TODO("Not yet implemented")
  }
}