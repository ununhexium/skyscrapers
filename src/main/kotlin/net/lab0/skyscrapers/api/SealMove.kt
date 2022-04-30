package net.lab0.skyscrapers.api

import net.lab0.skyscrapers.structure.Position

interface SealMove : Move {
  val seal: Position
}