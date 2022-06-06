package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.api.structure.ErrorMessage

sealed class JoiningError {
  object GameIsFull: JoiningError()
  data class GameNotFound(val errors: ErrorMessage): JoiningError()
}
