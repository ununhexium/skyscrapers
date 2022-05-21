package net.lab0.skyscrapers.engine.api

import net.lab0.skyscrapers.engine.structure.Position

interface Placement : Turn {
  val position: Position
}