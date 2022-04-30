package net.lab0.skyscrapers.exception

data class PlayerDoesntExist(val player: Int) :
  Exception("Player #$player doesn't exist")
