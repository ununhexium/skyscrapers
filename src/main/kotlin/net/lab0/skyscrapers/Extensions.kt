package net.lab0.skyscrapers

operator fun Array<Array<Height>>.get(pos: Position): Height =
  this[pos.x][pos.y]

operator fun Array<Array<Height>>.set(pos: Position, value: Int) {
  this[pos.x][pos.y] = Height(value)
}

