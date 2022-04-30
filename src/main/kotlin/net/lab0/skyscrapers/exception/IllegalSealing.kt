package net.lab0.skyscrapers.exception

import net.lab0.skyscrapers.structure.Position

data class IllegalSealing(
  val builder: Position,
  val seal: Position,
  val reason: String
) : Exception("Can't seal from $builder to $seal: $reason")
