package net.lab0.skyscrapers

import net.lab0.skyscrapers.api.BlocksData
import net.lab0.skyscrapers.api.Game
import net.lab0.skyscrapers.exception.InvalidBoardSize
import net.lab0.skyscrapers.exception.InvalidPlayersCount
import net.lab0.skyscrapers.rule.RuleBook

interface NewGame {
  fun new(
    width: Int = Defaults.WIDTH,
    height: Int = Defaults.HEIGHT,
    playerCount: Int = Defaults.PLAYER_COUNT,
    buildersPerPlayer: Int = Defaults.BUILDERS_PER_PLAYER,
    blocks: BlocksData = Defaults.BLOCKS,
    ruleBook: RuleBook = Defaults.RULE_BOOK
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