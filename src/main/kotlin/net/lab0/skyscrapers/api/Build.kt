package net.lab0.skyscrapers.api

import net.lab0.skyscrapers.structure.Position

interface Build : Move {
  val build: Position
}