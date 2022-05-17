package net.lab0.skyscrapers

import net.lab0.skyscrapers.logic.structure.Height
import net.lab0.skyscrapers.logic.structure.Position

operator fun Array<Array<Height>>.get(pos: Position): Height =
  this[pos.x][pos.y]

operator fun Array<Array<Height>>.set(pos: Position, value: Int) {
  this[pos.x][pos.y] = Height(value)
}

/**
 * Flips a NxM matrix into MxN matrix
 */
fun <T> List<List<T>>.flip(): List<List<T>> =
  (0 until this.first().size).map { x ->
    (0 until this.size).map { y ->
      this[y][x]
    }
  }

