package net.lab0.skyscrapers.server.integration

import net.lab0.skyscrapers.server.GameResponse
import net.lab0.skyscrapers.server.ServiceImpl
import net.lab0.skyscrapers.server.dto.BuildTurnDTO
import net.lab0.skyscrapers.server.dto.ConnectionResponse
import net.lab0.skyscrapers.server.dto.PhaseDTO
import net.lab0.skyscrapers.server.dto.PlaceTurnDTO
import net.lab0.skyscrapers.server.dto.PositionDTO
import net.lab0.skyscrapers.server.dto.TurnTypeDTO
import net.lab0.skyscrapers.server.dto.WinTurnDTO
import net.lab0.skyscrapers.server.routed
import org.assertj.core.api.Assertions.assertThat
import org.http4k.client.ApacheClient
import org.http4k.core.Body
import org.http4k.core.HttpTransaction
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.ClientFilters
import org.http4k.filter.ResponseFilters
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.junit.jupiter.api.Test
import net.lab0.skyscrapers.logic.structure.Position as P

class WinA2PlayersGame {
  val service = ServiceImpl.new()
  val port = 45678
  val server = routed(service).asServer(Undertow(port)).start()

  @Test
  fun `can win a 2 players game`() {

    val send = ClientFilters
      .SetBaseUriFrom(Uri.of("http://localhost:$port/api/v1"))
      .then(ApacheClient())

    // create game
    val gameCreation: GameResponse =
      send(Request(POST, "/games/foo")).parse()

    // connect as player 0
    val player0: ConnectionResponse =
      send(
        Request(
          POST,
          "/games/foo/connect"
        )
      ).parse()

    // connect as player 1
    val player1: ConnectionResponse =
      send(Request(POST, "/games/foo/connect")).parse()

    println(player0)
    println(player1)

    val authAs = { player: ConnectionResponse ->
      ClientFilters
        .SetBaseUriFrom(Uri.of("http://localhost:$port/api/v1"))
        .then(ClientFilters.BearerAuth(player.token))
        .then(ResponseFilters.ReportHttpTransaction { tx: HttpTransaction ->
          if(!tx.response.status.successful){
            println("Call to ${tx.request.uri} returned ${tx.response.status} and took ${tx.duration.toMillis()}")
            println(tx.response.bodyString())
          }

        })
        .then(ApacheClient())
    }

    val place = { player: ConnectionResponse, pos: P ->
      authAs(player)(
        Request(POST, "/games/foo/play").with(
          Body.auto<TurnTypeDTO>().toLens() of
              TurnTypeDTO.place(
                PlaceTurnDTO(player.player, PositionDTO(pos))
              )
        )
      ).parse<GameResponse>()
    }

    val build = { player: ConnectionResponse, start: P, target: P, build: P ->
      authAs(player)(
        Request(POST, "/games/foo/play").with(
          Body.auto<TurnTypeDTO>().toLens() of
              TurnTypeDTO.build(
                BuildTurnDTO(
                  player.player,
                  PositionDTO(start),
                  PositionDTO(target),
                  PositionDTO(build),
                )
              )
        )
      ).parse<GameResponse>()
    }

    val win = { player: ConnectionResponse, start: P, target: P ->
      authAs(player)(
        Request(POST, "/games/foo/play").with(
          Body.auto<TurnTypeDTO>().toLens() of
              TurnTypeDTO.win(
                WinTurnDTO(
                  player.player,
                  PositionDTO(start),
                  PositionDTO(target),
                )
              )
        )
      ).parse<GameResponse>()
    }

    val turn0: GameResponse = place(player0, P(1, 1))
    val turn1: GameResponse = place(player1, P(3, 1))
    val turn2: GameResponse = place(player0, P(3, 3))
    val turn3: GameResponse = place(player1, P(1, 3))

    // build round trip 1
    val turn4: GameResponse = build(player0, P(1, 1), P(2, 1), P(1, 1))
    val turn5: GameResponse = build(player1, P(3, 1), P(3, 2), P(3, 1))
    val turn6: GameResponse = build(player0, P(2, 1), P(1, 1), P(2, 1))
    val turn7: GameResponse = build(player1, P(3, 2), P(3, 1), P(3, 2))

    // build round trip 2
    val turn8: GameResponse = build(player0, P(1, 1), P(2, 1), P(1, 1))
    val turn9: GameResponse = build(player1, P(3, 1), P(3, 2), P(3, 1))
    val turn10: GameResponse = build(player0, P(2, 1), P(1, 1), P(2, 1))
    val turn11: GameResponse = build(player1, P(3, 2), P(3, 1), P(3, 2))

    // build round trip 3
    val turn12: GameResponse = build(player0, P(1, 1), P(2, 1), P(1, 1))
    val turn13: GameResponse = build(player1, P(3, 1), P(3, 2), P(3, 1))
    val turn14: GameResponse = win(player0, P(2, 1), P(1, 1))

    // player0 wins
    val finalResponse: GameResponse = send(Request(GET, "/games/foo")).parse()
    assertThat(finalResponse.state.phase).isEqualTo(PhaseDTO.FINISHED)
  }

  inline fun <reified T : Any> Response.parse(): T {
    return Body.auto<T>().toLens().extract(this)
  }
}