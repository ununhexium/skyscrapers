package net.lab0.skyscrapers.logic.api

import net.lab0.skyscrapers.logic.structure.Position

interface Seal : MoveOnly {
  val seal: Position
}