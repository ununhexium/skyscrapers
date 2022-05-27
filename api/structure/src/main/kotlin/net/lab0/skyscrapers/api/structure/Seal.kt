package net.lab0.skyscrapers.api.structure

import net.lab0.skyscrapers.api.structure.Position

interface Seal : MoveOnly {
  val seal: Position
}