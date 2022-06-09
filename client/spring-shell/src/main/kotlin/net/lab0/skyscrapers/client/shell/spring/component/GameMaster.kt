package net.lab0.skyscrapers.client.shell.spring.component

import arrow.core.merge
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.client.http.ClientError
import net.lab0.skyscrapers.client.http.SkyscraperClient
import net.lab0.skyscrapers.client.shell.spring.BaseUrl
import net.lab0.skyscrapers.client.shell.spring.ShellState
import net.lab0.skyscrapers.client.shell.spring.SkyscraperClientFactoryComponent
import org.springframework.stereotype.Component

/**
 * Manages the info that outlives commands.
 */
@Component
class GameMaster(val factory: SkyscraperClientFactoryComponent) {
  var state = ShellState()

  val inGame
    get() = state.currentGame != null

  /**
   * For test and debugging
   */
  fun forceState(state: ShellState) {
    this.state = state
  }

  fun reconnect(baseUrl: String) {
    state = state.copy(client = factory.newClient(BaseUrl(baseUrl)))
  }

  fun status(baseUrl: String): String? {
    return state
      .client
      ?.status()
      ?.map {
        "Connected to $baseUrl.\nAvailable games: " +
            it.games.joinToString(separator = "\n")
      }
      ?.mapLeft { status ->
        "Failed to connect to $baseUrl with status $status."
      }
      ?.merge()
  }

  fun create(name: GameName): String? {
    return state
      .client
      ?.create(name)
      ?.map {
        "Created game ${it.name}."
      }
      ?.mapLeft {
        "Error when creating the game:\n" + it.joinToString(separator = "\n")
      }
      ?.merge()
  }

  // TODO: show error message when the shell is not connected (or use availability to mark this command?)
  fun join(name: GameName): String? {
    return state.client?.join(name)
      ?.map {
        state = state.copy(
          accessToken = it.token,
          currentGame = name,
        )

        "Joined game ${name.value} as player ${it.player} with access token ${it.token.value}."
      }
      ?.mapLeft {
        "Error when joining the game:\n" + it.joinToString(separator = "\n")
      }
      ?.merge()
  }

  fun place(position: Position): String? {
    checkInGame()

    return state.client
      ?.place(state.currentGame!!, state.accessToken!!, position)
      ?.map {
        "Placed a builder at ${position.toString(Position.Style.COMA)}."
      }
      ?.mapLeft {
        "Error when playing the game:\n" + when (it) {
          // TODO: the output should be a sealed class that separates the normal string and the error responses
          is ClientError.GameRuleErrors -> it.violations.joinToString(separator = "\n")
          is ClientError.SimpleErrors -> it.errors.joinToString(separator = "\n")
        }
      }
      ?.merge()
  }

  private fun checkInGame() {
    check(inGame) { "Can't play before joining a game." }
  }

  fun build(start: Position, target: Position, build: Position): String? {
    checkInGame()

    return state
      .client
      ?.build(state.currentGame!!, state.accessToken!!, start, target, build)
      ?.map {
        "Moved builder from ${start.toString(Position.Style.COMA)} " +
            "to ${target.toString(Position.Style.COMA)} " +
            "and built at ${build.toString(Position.Style.COMA)}."
      }
      ?.mapLeft {
        "Error when playing the game:\n" + when (it) {
          // TODO: the output should be a sealed class that separates the normal string and the error responses
          is ClientError.GameRuleErrors -> it.violations.joinToString(separator = "\n")
          is ClientError.SimpleErrors -> it.errors.joinToString(separator = "\n")
        }
      }
      ?.merge()
  }

  fun seal(start: Position, target: Position, seal: Position): String? {
    checkInGame()

    return state
      .client
      ?.seal(state.currentGame!!, state.accessToken!!, start, target, seal)
      ?.map {
        "Moved builder from ${start.toString(Position.Style.COMA)} " +
            "to ${target.toString(Position.Style.COMA)} " +
            "and sealed at ${seal.toString(Position.Style.COMA)}."
      }
      ?.mapLeft {
        "Error when playing the game:\n" + when (it) {
          // TODO: the output should be a sealed class that separates the normal string and the error responses
          is ClientError.GameRuleErrors -> it.violations.joinToString(separator = "\n")
          is ClientError.SimpleErrors -> it.errors.joinToString(separator = "\n")
        }
      }
      ?.merge()
  }

  fun win(start: Position, target: Position): String? {
    checkInGame()

    return state
      .client
      ?.win(state.currentGame!!, state.accessToken!!, start, target)
      ?.map {
        "Moved builder from ${start.toString(Position.Style.COMA)} " +
            "to ${target.toString(Position.Style.COMA)} and won."
      }
      ?.mapLeft {
        "Error when playing the game:\n" + when (it) {
          // TODO: the output should be a sealed class that separates the normal string and the error responses
          is ClientError.GameRuleErrors -> it.violations.joinToString(separator = "\n")
          is ClientError.SimpleErrors -> it.errors.joinToString(separator = "\n")
        }
      }
      ?.merge()
  }

}
