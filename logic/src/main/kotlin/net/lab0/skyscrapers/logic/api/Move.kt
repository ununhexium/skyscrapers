package net.lab0.skyscrapers.logic.api

import net.lab0.skyscrapers.logic.structure.Position

interface Move {
  companion object {
    fun make(
      start: Position,
      target: Position,
    ) = object: Move {
      override val start = start
      override val target = target
    }
  }

  val start: Position
  val target: Position
}
