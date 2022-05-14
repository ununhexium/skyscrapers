package net.lab0.skyscrapers.server.exception

import net.lab0.skyscrapers.server.value.GameName

class GameNotFound(gameName: GameName) : Exception("The is no game named $gameName")
