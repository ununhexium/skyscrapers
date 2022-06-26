package net.lab0.skyscrapers.engine

import net.lab0.skyscrapers.api.structure.BlocksData
import net.lab0.skyscrapers.api.structure.Height
import net.lab0.skyscrapers.engine.api.Game
import net.lab0.skyscrapers.engine.rule.RuleBook

interface GameFactory {
  fun new(
    width: Int = Defaults.WIDTH,
    height: Int = Defaults.HEIGHT,
    playerCount: Int = Defaults.PLAYER_COUNT,
    buildersPerPlayer: Int = Defaults.BUILDERS_PER_PLAYER,
    blocks: BlocksData = Defaults.BLOCKS,
    ruleBook: RuleBook = Defaults.RULE_BOOK
  ): Game

  /**
   * A minimalistic 2 player game on a 2*2 grid
   */
  fun newNano(
    ruleBook: RuleBook = Defaults.RULE_BOOK
  ) = new(
    width = 2,
    height = 2,
    playerCount = 2,
    buildersPerPlayer = 1,
    blocks = BlocksData(
      mapOf(
        Height.SEAL to 1,
        Height(1) to 4,
        Height(2) to 3,
        Height(3) to 2,
      )
    ),
  )
}
