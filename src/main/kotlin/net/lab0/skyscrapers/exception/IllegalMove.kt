package net.lab0.skyscrapers.exception

import net.lab0.skyscrapers.Position

class IllegalMove(
  start: Position,
  target: Position,
  reason: String
) : Exception("Can't move from $start to $target: $reason")
