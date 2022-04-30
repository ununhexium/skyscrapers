package net.lab0.skyscrapers.api

import net.lab0.skyscrapers.structure.Position

interface Move : Turn {
  val start: Position
  val target: Position
  val sealOrBuild: Position
}