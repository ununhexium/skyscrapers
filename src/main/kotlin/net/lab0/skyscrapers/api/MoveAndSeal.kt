package net.lab0.skyscrapers.api

import net.lab0.skyscrapers.structure.Position

interface MoveAndSeal : Move {
  val seal: Position
}