package net.lab0.skyscrapers

import net.lab0.skyscrapers.api.BlocksData
import net.lab0.skyscrapers.rule.CheckCurrentPlayer
import net.lab0.skyscrapers.rule.PhaseRule
import net.lab0.skyscrapers.rule.RuleBook
import net.lab0.skyscrapers.rule.move.BoardMoveContainmentRule
import net.lab0.skyscrapers.rule.move.ClimbingRule
import net.lab0.skyscrapers.rule.move.DefaultBuildersMovementRule
import net.lab0.skyscrapers.rule.move.MovementRangeRule
import net.lab0.skyscrapers.rule.move.SealsPreventMovingRule
import net.lab0.skyscrapers.rule.move.build.BlocksAvailabilityRule
import net.lab0.skyscrapers.rule.move.build.BoardBuildingContainmentRule
import net.lab0.skyscrapers.rule.move.build.BuildersPreventsBuildingRule
import net.lab0.skyscrapers.rule.move.build.BuildingRangeRule
import net.lab0.skyscrapers.rule.move.build.SealsPreventBuildingRule
import net.lab0.skyscrapers.rule.move.seal.BoardSealingContainmentRule
import net.lab0.skyscrapers.rule.move.seal.BuildersPreventsSealingRule
import net.lab0.skyscrapers.rule.move.seal.SealingRangeRule
import net.lab0.skyscrapers.rule.move.seal.SealsPreventSealingRule
import net.lab0.skyscrapers.rule.move.win.WinConditionRule
import net.lab0.skyscrapers.rule.placement.BoardPlacementContainmentRule
import net.lab0.skyscrapers.rule.placement.CantGiveUpDuringPlacementRule
import net.lab0.skyscrapers.rule.placement.PlaceBuilderOnEmptyCell
import net.lab0.skyscrapers.structure.Bounds
import net.lab0.skyscrapers.structure.Height

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
      CantGiveUpDuringPlacementRule,
    ),

    placementRules = listOf(
      BoardPlacementContainmentRule,
      PlaceBuilderOnEmptyCell,
    ),

    moveRules = listOf(
      BoardMoveContainmentRule,
      DefaultBuildersMovementRule,
      MovementRangeRule(),
      ClimbingRule(),
      SealsPreventMovingRule,
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
