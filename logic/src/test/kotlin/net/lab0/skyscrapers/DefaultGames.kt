package net.lab0.skyscrapers

import net.lab0.skyscrapers.action.DSL
import net.lab0.skyscrapers.api.BlocksData
import net.lab0.skyscrapers.api.Game
import net.lab0.skyscrapers.structure.Position

object DefaultGames {

  fun newGameWithSequentiallyPlacedBuilders(
    width: Int = Defaults.WIDTH,
    height: Int = Defaults.HEIGHT,
    playerCount: Int = Defaults.PLAYER_COUNT,
    buildersPerPlayer: Int = Defaults.BUILDERS_PER_PLAYER,
    blocks: BlocksData = Defaults.BLOCKS,
  ) = Game.new(width, height, playerCount, buildersPerPlayer, blocks).also {
    (0 until playerCount * buildersPerPlayer).forEach { turn ->
      val player = turn % playerCount
      val x = turn % width
      val y = turn / height
      it.play(
        DSL.player(player).placement.addBuilder(Position(x, y))
      )
    }
  }

}
