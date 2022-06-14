package net.lab0.skyscrapers.client.shell.spring.component

import arrow.core.merge
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.client.http.ClientError
import net.lab0.skyscrapers.client.http.SkyscraperClient
import net.lab0.skyscrapers.client.shell.spring.BaseUrl
import net.lab0.skyscrapers.client.shell.spring.InternalGameAccessState
import net.lab0.skyscrapers.client.shell.spring.SkyscraperClientFactoryComponent
import net.lab0.skyscrapers.client.shell.spring.data.HierarchyResult
import net.lab0.skyscrapers.client.shell.spring.data.ShellResult
import net.lab0.skyscrapers.client.shell.spring.data.ShellResult.Ok
import net.lab0.skyscrapers.client.shell.spring.data.ShellResult.Problem
import org.springframework.stereotype.Component

/**
 * Manages the info that outlives commands.
 */
@Component
class GameAccessManager(val factory: SkyscraperClientFactoryComponent) {
  private var state = InternalGameAccessState()

  val currentGame: String?
    get() = state.currentGame?.value

  val inGame
    get() = state.inGame

  fun isConnected() =
    state.isConnected()

  /**
   * For test and debugging
   */
  internal fun forceState(state: InternalGameAccessState) {
    this.state = state
  }

  fun reconnect(baseUrl: BaseUrl) {
    state = InternalGameAccessState(
      baseUrl = baseUrl,
      client = factory.newClient(baseUrl)
    )
  }

  fun status(): ShellResult =
    state.useClient { client ->
      client.status()
        .map {
          Ok.Text(
            if (it.games.isEmpty()) "No game available."
            else "Available games:\n" +
                it.games.sorted().joinToString(separator = "\n")
          )
        }
        .mapLeft { status ->
          Problem.Text(
            "Failed to query ${state.baseUrl?.value} with status $status."
          )
        }
        .merge()
    } ?: Problem.Text("Not connected.")

  fun create(name: GameName): ShellResult =
    state.useClient { client ->
      client
        .create(name)
        .map {
          Ok.Text("Created the game ${it.name}.")
        }
        .mapLeft {
          Problem.Text(
            "Error when creating the game:\n" + it.joinToString(
              separator = "\n"
            )
          )
        }
        .merge()
    } ?: Problem.Text("Connect to a server before creating a game.")

  fun join(name: GameName): ShellResult =
    state.useClient { client ->
      client
        .join(name)
        .map {
          state = state.copy(
            accessToken = it.token,
            currentGame = name,
          )

          Ok.Text(
            "Joined the game ${name.value} as player ${it.player} with access token ${it.token.value}."
          )
        }
        .mapLeft {
          Problem.Text(
            "Error when joining the game ${name.value}:\n" + it.joinToString(separator = "\n")
          )
        }
        .merge()
    } ?: Problem.Text("Connect to a server before joining a game.")

  fun place(position: Position): HierarchyResult =
    state.useClient { client ->
      client
        .place(
          state.currentGame!!,
          state.accessToken!!,
          position
        )
        .map {
          HierarchyResult.StateUpdate(
            it,
            "Placed a builder at ${position.toString(Position.Style.COMA)}."
          )
        }
        .mapLeft {
          HierarchyResult.Error(it)
        }
        .merge()
    } ?: throw IllegalStateException()

  fun build(start: Position, target: Position, build: Position): String =
    state.useClient { client ->
      client
        .build(
          state.currentGame!!,
          state.accessToken!!,
          start,
          target,
          build
        )
        .map {
          "Moved builder from ${start.toString(Position.Style.COMA)} " +
              "to ${target.toString(Position.Style.COMA)} " +
              "and built at ${build.toString(Position.Style.COMA)}."
        }
        .mapLeft {
          "Error when playing the game:\n" + when (it) {
            // TODO: the output should be a sealed class that separates the normal string and the error responses
            is ClientError.GameRuleErrors -> it.violations.joinToString(
              separator = "\n"
            )
            is ClientError.SimpleErrors -> it.errors.joinToString(separator = "\n")
          }
        }
        .merge()
    } ?: throw IllegalStateException()

  fun seal(start: Position, target: Position, seal: Position): String =
    state.useClient { client ->
      client
        .seal(
          state.currentGame!!,
          state.accessToken!!,
          start,
          target,
          seal
        )
        .map {
          "Moved builder from ${start.toString(Position.Style.COMA)} " +
              "to ${target.toString(Position.Style.COMA)} " +
              "and sealed at ${seal.toString(Position.Style.COMA)}."
        }
        .mapLeft {
          "Error when playing the game:\n" + when (it) {
            // TODO: the output should be a sealed class that separates the normal string and the error responses
            is ClientError.GameRuleErrors -> it.violations.joinToString(
              separator = "\n"
            )
            is ClientError.SimpleErrors -> it.errors.joinToString(separator = "\n")
          }
        }
        .merge()
    } ?: throw IllegalStateException()

  fun win(start: Position, target: Position): String =
    state.useClient { client ->
      client
        .win(
          state.currentGame!!,
          state.accessToken!!,
          start,
          target
        )
        .map {
          "Moved builder from ${start.toString(Position.Style.COMA)} " +
              "to ${target.toString(Position.Style.COMA)} and won."
        }
        .mapLeft {
          "Error when playing the game:\n" + when (it) {
            // TODO: the output should be a sealed class that separates the normal string and the error responses
            is ClientError.GameRuleErrors -> it.violations.joinToString(
              separator = "\n"
            )
            is ClientError.SimpleErrors -> it.errors.joinToString(separator = "\n")
          }
        }
        .merge()
    } ?: throw IllegalStateException()

  fun state(): String? {
    return state
      .client
      ?.state(state.currentGame!!)
      ?.map {
        it.toCompositeString()
      }
      ?.mapLeft {
        "Error when joining the game:\n" + it.joinToString(separator = "\n")
      }
      ?.merge()
  }
}
