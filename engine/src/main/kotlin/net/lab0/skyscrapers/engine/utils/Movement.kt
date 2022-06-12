package net.lab0.skyscrapers.engine.utils

import net.lab0.skyscrapers.api.structure.MoveOnly
import net.lab0.skyscrapers.api.structure.Position

data class Movement(
  override val start: Position,
  override val target: Position
) : MoveOnly
