package net.lab0.skyscrapers.exception

class PlayerDoesntExist(player: Int) :
  Exception("Player #$player doesn't exist")
