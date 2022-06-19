package net.lab0.skyscrapers.client.http

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.BuildTurnDTO
import net.lab0.skyscrapers.api.dto.ConnectionResponse
import net.lab0.skyscrapers.api.dto.ErrorResponse
import net.lab0.skyscrapers.api.dto.GameResponse
import net.lab0.skyscrapers.api.dto.GameStateDTO
import net.lab0.skyscrapers.api.dto.GameViolationsDTO
import net.lab0.skyscrapers.api.dto.HistoryResponseDTO
import net.lab0.skyscrapers.api.dto.ListGamesResponse
import net.lab0.skyscrapers.api.dto.PlaceTurnDTO
import net.lab0.skyscrapers.api.dto.PositionDTO
import net.lab0.skyscrapers.api.dto.SealTurnDTO
import net.lab0.skyscrapers.api.dto.StatusResponse
import net.lab0.skyscrapers.api.dto.WinTurnDTO
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.http4k.AUTHORIZATION
import net.lab0.skyscrapers.api.http4k.Authorization
import net.lab0.skyscrapers.api.structure.Errors
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.client.http.ClientError.GameRuleErrors
import net.lab0.skyscrapers.client.http.ClientError.SimpleErrors
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.lens.Header

/**
 * @param handler that targets the server (no /api/v1/...)
 */
class SkyscraperClientImpl(
  val handler: HttpHandler,
) : SkyscraperClient {
  override fun status(): Either<Status, StatusResponse> {
    val req = Request(Method.GET, "/api/v1/status")
    val res = handler(req)
    return if (res.status == Status.OK) {
      Right(res.extract())
    } else {
      Left(res.status)
    }
  }

  override fun state(name: GameName): Either<Errors, GameState> {
    val req = Request(Method.GET, "/api/v1/games" / name)
    val res = handler(req)

    if (res.status == Status.OK) {
      return Right(res.extract<GameResponse>().state.toModel())
    } else {
      return Left(res.extract<ErrorResponse>().errors)
    }
  }

  override fun listGames(): List<GameName> {
    val req = Request(Method.GET, "/api/v1/games")
    val res = handler(req)

    val list = Body.auto<ListGamesResponse>().toLens()(res)
    return list.names.map { GameName(it) }
  }

  override fun create(name: GameName): Either<List<String>, GameResponse> {
    val req = Request(Method.POST, "/api/v1/games" / name)
    val res = handler(req)

    return if (res.status == Status.CREATED) {
      Right(res.extract())
    } else {
      Left(res.extract<ErrorResponse>().errors)
    }
  }

  override fun join(name: GameName): Either<Errors, ConnectionResponse> {
    val req = Request(Method.POST, "/api/v1/games" / name / "join")
    val res = handler(req)

    return if (res.status == Status.CREATED) {
      Right(res.extract())
    } else {
      Left(res.extract<ErrorResponse>().errors)
    }
  }

  override fun place(
    name: GameName,
    token: AccessToken,
    position: Position
  ): Either<ClientError, GameState> {
    val req = Request(Method.POST, "/api/v1/games" / name / "place").with(
      Body.auto<PlaceTurnDTO>().toLens() of PlaceTurnDTO(PositionDTO(position)),
      Header.AUTHORIZATION of Authorization.Bearer(token)
    )
    val res = handler(req)

    return when (res.status) {
      Status.CREATED -> Right(res.extract<GameStateDTO>().toModel())
      Status.CONFLICT -> Left(GameRuleErrors(res.extract<GameViolationsDTO>().violations.map { it.toModel() }))
      else -> Left(SimpleErrors(res.extract<ErrorResponse>().errors))
    }
  }

  override fun build(
    name: GameName,
    token: AccessToken,
    start: Position,
    target: Position,
    build: Position
  ): Either<ClientError, GameState> {
    val req = Request(Method.POST, "/api/v1/games" / name / "build").with(
      Body.auto<BuildTurnDTO>().toLens() of BuildTurnDTO(start, target, build),
      Header.AUTHORIZATION of Authorization.Bearer(token)
    )

    val res = handler(req)

    return when (res.status) {
      Status.CREATED -> Right(res.extract<GameStateDTO>().toModel())
      Status.CONFLICT -> Left(GameRuleErrors(res.extract<GameViolationsDTO>().violations.map { it.toModel() }))
      else -> Left(SimpleErrors(res.extract<ErrorResponse>().errors))
    }
  }

  override fun seal(
    name: GameName,
    token: AccessToken,
    start: Position,
    target: Position,
    seal: Position
  ): Either<ClientError, GameState> {
    val req = Request(Method.POST, "/api/v1/games" / name / "seal").with(
      Body.auto<SealTurnDTO>().toLens() of SealTurnDTO(start, target, seal),
      Header.AUTHORIZATION of Authorization.Bearer(token)
    )

    val res = handler(req)

    return when (res.status) {
      Status.CREATED -> Right(res.extract<GameStateDTO>().toModel())
      Status.CONFLICT -> Left(GameRuleErrors(res.extract<GameViolationsDTO>().violations.map { it.toModel() }))
      else -> Left(SimpleErrors(res.extract<ErrorResponse>().errors))
    }
  }

  override fun win(
    name: GameName,
    token: AccessToken,
    start: Position,
    target: Position
  ): Either<ClientError, GameState> {
    val req = Request(Method.POST, "/api/v1/games" / name / "win").with(
      Body.auto<WinTurnDTO>().toLens() of WinTurnDTO(start, target),
      Header.AUTHORIZATION of Authorization.Bearer(token)
    )

    val res = handler(req)

    return when (res.status) {
      Status.CREATED -> Right(res.extract<GameStateDTO>().toModel())
      Status.CONFLICT -> Left(GameRuleErrors(res.extract<GameViolationsDTO>().violations.map { it.toModel() }))
      else -> Left(SimpleErrors(res.extract<ErrorResponse>().errors))
    }
  }

  override fun history(name: GameName): Either<ClientError, List<GameState>> {
    val req = Request(Method.GET, "/api/v1/games" / name / "history")

    val res = handler(req)

    return when (res.status) {
      Status.OK -> Right(res.extract<HistoryResponseDTO>().toModel())
      else -> Left(SimpleErrors(res.extract<ErrorResponse>().errors))
    }
  }
}
