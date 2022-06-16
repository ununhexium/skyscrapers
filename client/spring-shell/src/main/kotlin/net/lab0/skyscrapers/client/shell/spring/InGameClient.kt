package net.lab0.skyscrapers.client.shell.spring

import arrow.core.partially1
import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.client.http.SkyscraperClient

class InGameClient(val client: SkyscraperClient, val game:GameName, val accessToken: AccessToken) {
  fun state() = client.state(game)

  fun place(
    position: Position,
  ) = client::place.partially1(game).partially1(accessToken)

  fun build(
    start: Position,
    target: Position,
    build: Position,
  ) = client::build.partially1(game).partially1(accessToken)

  fun seal(
    start: Position,
    target: Position,
    seal: Position,
  ) = client::seal.partially1(game).partially1(accessToken)

  fun win(
    start: Position,
    target: Position,
  ) = client::win.partially1(game).partially1(accessToken)
}
