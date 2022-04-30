package net.lab0.skyscrapers.exception

import net.lab0.skyscrapers.structure.Position

data class IllegalBuilding(
  val builder: Position,
  val building: Position,
  val reason: String
) : Exception("Can't build from $builder to $building: $reason")
