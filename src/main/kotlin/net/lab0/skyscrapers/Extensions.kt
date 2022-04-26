package net.lab0.skyscrapers

operator fun Array<Array<Height>>.get(pos: Position): Height =
  this[pos.x][pos.y]

