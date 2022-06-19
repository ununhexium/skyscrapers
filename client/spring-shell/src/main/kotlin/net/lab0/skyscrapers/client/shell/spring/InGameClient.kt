package net.lab0.skyscrapers.client.shell.spring

import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.client.http.SkyscraperClient

class InGameClient(
  val client: SkyscraperClient,
  val game: GameName,
  val accessToken: AccessToken
) {
  fun state() = client.state(game)

  fun history() = client.history(game)

  fun place(
    position: Position,
  ) = client.place(game, accessToken, position)

  fun build(
    start: Position,
    target: Position,
    build: Position,
  ) = client.build(game, accessToken, start, target, build)

  fun seal(
    start: Position,
    target: Position,
    seal: Position,
  ) = client.seal(game, accessToken, start, target, seal)

  fun win(
    start: Position,
    target: Position,
  ) = client.win(game, accessToken, start, target)
}
