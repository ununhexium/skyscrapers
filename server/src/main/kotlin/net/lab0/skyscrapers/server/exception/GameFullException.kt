package net.lab0.skyscrapers.server.exception

import net.lab0.skyscrapers.server.value.GameName

class GameFullException(gameName: GameName) : Exception("The game $gameName is full.")
