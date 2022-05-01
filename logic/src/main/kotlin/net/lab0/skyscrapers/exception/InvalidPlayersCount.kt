package net.lab0.skyscrapers.exception

class InvalidPlayersCount(count: Int) :
  InvalidConfiguration("The minimum number of players is 1. You gave $count")
