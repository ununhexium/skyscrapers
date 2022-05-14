package net.lab0.skyscrapers.logic.exception

class InvalidBoardSize(width: Int, height: Int, builders: Int) :
  InvalidConfiguration("The board size ${width}x${height} with ${width * height} cells is too small for the number of builders: $builders")