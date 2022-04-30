package net.lab0.skyscrapers.exception

import net.lab0.skyscrapers.structure.Position

data class IllegalMove(
  val start: Position,
  val target: Position,
  val reason: String
) : Exception("Can't move from $start to $target: $reason")
