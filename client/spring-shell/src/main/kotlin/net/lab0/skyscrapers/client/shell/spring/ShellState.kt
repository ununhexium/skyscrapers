package net.lab0.skyscrapers.client.shell.spring

import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.client.http.SkyscraperClient

data class ShellState(
  val client: SkyscraperClient? = null,
  val currentGame: GameName? = null,
  val accessToken: AccessToken? = null,
)
