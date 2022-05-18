package net.lab0.skyscrapers.engine

import net.lab0.skyscrapers.engine.api.BlocksData
import net.lab0.skyscrapers.engine.api.MoveOnly
import net.lab0.skyscrapers.engine.api.Move
import net.lab0.skyscrapers.engine.api.Rule
import net.lab0.skyscrapers.engine.rule.CheckCurrentPlayer
import net.lab0.skyscrapers.engine.rule.PhaseRule
import net.lab0.skyscrapers.engine.rule.RuleBook
import net.lab0.skyscrapers.engine.rule.move.BoardMoveContainmentRule
import net.lab0.skyscrapers.engine.rule.move.ClimbingRule
import net.lab0.skyscrapers.engine.rule.move.BuildersMoveToEmptyCells
import net.lab0.skyscrapers.engine.rule.move.MovementRangeRule
import net.lab0.skyscrapers.engine.rule.move.PlayersMoveTheirOwnBuilders
import net.lab0.skyscrapers.engine.rule.move.SealsPreventMovingRule
import net.lab0.skyscrapers.engine.rule.move.building.BlocksAvailabilityRule
import net.lab0.skyscrapers.engine.rule.move.building.BoardBuildingContainmentRule
import net.lab0.skyscrapers.engine.rule.move.building.BuildersPreventsBuildingRule
import net.lab0.skyscrapers.engine.rule.move.building.BuildingRangeRule
import net.lab0.skyscrapers.engine.rule.move.building.SealsPreventBuildingRule
import net.lab0.skyscrapers.engine.rule.move.seal.BoardSealingContainmentRule
import net.lab0.skyscrapers.engine.rule.move.seal.BuildersPreventsSealingRule
import net.lab0.skyscrapers.engine.rule.move.seal.SealingRangeRule
import net.lab0.skyscrapers.engine.rule.move.seal.SealsPreventSealingRule
import net.lab0.skyscrapers.engine.rule.move.win.WinConditionRule
import net.lab0.skyscrapers.engine.rule.placement.BoardPlacementContainmentRule
import net.lab0.skyscrapers.engine.rule.placement.PlaceBuilderOnEmptyCell
import net.lab0.skyscrapers.engine.structure.Bounds
import net.lab0.skyscrapers.engine.structure.Height

object Defaults {
  const val WIDTH = 5
  const val HEIGHT = 5
  val BOUNDS = Bounds(WIDTH, HEIGHT)
  const val PLAYER_COUNT = 2

  const val BUILDERS_PER_PLAYER = 2

  val BLOCKS = BlocksData(
    mapOf(
      Height(0) to 17,
      Height(1) to 21,
      Height(2) to 19,
      Height(3) to 14,
    )
  )

  val RULE_BOOK = RuleBook(
    turnRules = listOf(
      PhaseRule,
      CheckCurrentPlayer,
    ),

    placementRules = listOf(
      BoardPlacementContainmentRule,
      PlaceBuilderOnEmptyCell,
    ),

    moveOnlyRules = listOf<Rule<MoveOnly>>(
      BoardMoveContainmentRule,
      BuildersMoveToEmptyCells,
      MovementRangeRule(),
      ClimbingRule(),
      SealsPreventMovingRule,
    ),

    moveRules = listOf<Rule<Move>>(
      PlayersMoveTheirOwnBuilders,
    ),

    buildRules = listOf(
      BoardBuildingContainmentRule,
      BlocksAvailabilityRule(),
      BuildingRangeRule(),
      BuildersPreventsBuildingRule,
      SealsPreventBuildingRule,
    ),

    sealRules = listOf(
      BoardSealingContainmentRule,
      SealingRangeRule(),
      SealsPreventSealingRule,
      BuildersPreventsSealingRule,
    ),

    winRules = listOf(
      WinConditionRule,
    ),
  )
}
