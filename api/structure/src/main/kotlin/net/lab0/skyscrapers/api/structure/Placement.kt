package net.lab0.skyscrapers.api.structure

import net.lab0.skyscrapers.api.structure.Position

interface Placement : Turn {
  val position: Position
}