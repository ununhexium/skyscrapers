package net.lab0.skyscrapers.logic.exception

class InvalidPlayersCount(count: Int) :
  InvalidConfiguration("The minimum number of players is 1. You gave $count")
