package net.lab0.skyscrapers.engine

import net.lab0.skyscrapers.api.dto.RuleBook
import net.lab0.skyscrapers.api.structure.BlocksData
import net.lab0.skyscrapers.engine.api.Game
import net.lab0.skyscrapers.engine.exception.InvalidBoardSize
import net.lab0.skyscrapers.engine.exception.InvalidPlayersCount

open class GameFactoryImpl : GameFactory {
  override fun new(
    width: Int,
    height: Int,
    playerCount: Int,
    buildersPerPlayer: Int,
    blocks: BlocksData,
    ruleBook: RuleBook,
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
      initialBlocks = blocks,
      ruleBook = ruleBook
    )
  }
}
