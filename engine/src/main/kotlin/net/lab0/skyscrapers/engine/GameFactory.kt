package net.lab0.skyscrapers.engine

import net.lab0.skyscrapers.engine.api.BlocksData
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
}
