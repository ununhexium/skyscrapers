package net.lab0.skyscrapers.server.exception

import net.lab0.skyscrapers.api.dto.value.GameName

class GameNotFound(gameName: GameName) : Exception("The is no game named $gameName")
