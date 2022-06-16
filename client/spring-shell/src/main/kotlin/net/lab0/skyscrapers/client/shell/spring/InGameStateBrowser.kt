package net.lab0.skyscrapers.client.shell.spring

import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.engine.utils.StateBrowser

class InGameStateBrowser(
  val playerId: Int,
  val stateBrowser: StateBrowser
) {
  fun getBuilderPositionsForPlayer() =
    stateBrowser.getBuilderPositionsForPlayer(playerId)

  fun getMovableBuilders() =
    stateBrowser.getMovableBuilders(playerId)

  fun builderCanMoveTo(builder: Position, target: Position) =
    stateBrowser.builderCanMoveTo(builder, target)

  fun getWinnableBuilders() =
    stateBrowser.getWinnableBuilders(playerId)

  fun getTargetPositions() =
    stateBrowser.getTargetPositions(playerId)

  fun getPlaceableTurns() =
    stateBrowser.getPlaceableTurns(playerId)

  fun getBuildableTurns() =
    stateBrowser.getBuildableTurns(playerId)
}
