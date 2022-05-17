package net.lab0.skyscrapers.logic.api

import net.lab0.skyscrapers.logic.structure.Position

interface Placement : Turn {
  val position: Position
}