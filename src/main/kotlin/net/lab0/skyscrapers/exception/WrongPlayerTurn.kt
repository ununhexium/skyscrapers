package net.lab0.skyscrapers.exception

class WrongPlayerTurn(actual: Int, expected: Int) :
  Exception("Player #$actual played but it was player #$expected's turn.")
