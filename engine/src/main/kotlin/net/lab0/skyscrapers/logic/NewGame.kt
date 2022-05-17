package net.lab0.skyscrapers.logic

import net.lab0.skyscrapers.logic.api.BlocksData
import net.lab0.skyscrapers.logic.api.Game
import net.lab0.skyscrapers.logic.exception.InvalidBoardSize
import net.lab0.skyscrapers.logic.exception.InvalidPlayersCount
import net.lab0.skyscrapers.logic.rule.RuleBook

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
