package net.lab0.skyscrapers.exception

import net.lab0.skyscrapers.Position

class CellUsedByAnotherBuilder(cellPosition: Position) :
  Exception("Can't move to cell $cellPosition as it's already occupied by another builder")
