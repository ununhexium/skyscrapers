package net.lab0.skyscrapers

import net.lab0.skyscrapers.exception.InvalidBoardSize
import net.lab0.skyscrapers.exception.InvalidPlayersCount

interface NewGame {
  fun new(
    width: Int = 5,
    height: Int = 5,
    playerCount: Int = 2,
    buildersPerPlayer: Int = 2,
  ): Game {
    if (playerCount < 1)
      throw InvalidPlayersCount(playerCount)

    val cells = width * height
    val builders = playerCount * buildersPerPlayer
    if (buildersPerPlayer < 0 || width < 0 || height < 0 || builders >= cells) {
      throw InvalidBoardSize(width, height, builders)
    }

    return GameImpl(
      width = width,
      height = height,
      playerCount = playerCount,
      maxBuildersPerPlayer = buildersPerPlayer,
    )
  }
}