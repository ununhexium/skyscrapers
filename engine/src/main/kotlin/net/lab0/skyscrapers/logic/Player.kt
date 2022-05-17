package net.lab0.skyscrapers.logic

data class Player(val id: Int, var active: Boolean) {
  constructor(id: Int) : this(id, true)
}