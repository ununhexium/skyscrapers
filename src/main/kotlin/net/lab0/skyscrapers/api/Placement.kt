package net.lab0.skyscrapers.api

import net.lab0.skyscrapers.structure.Position

interface Placement : Turn {
  val position: Position
}