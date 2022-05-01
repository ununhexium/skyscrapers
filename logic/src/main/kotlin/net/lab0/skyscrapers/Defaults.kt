package net.lab0.skyscrapers

import net.lab0.skyscrapers.api.BlocksData
import net.lab0.skyscrapers.rule.CheckCurrentPlayer
import net.lab0.skyscrapers.rule.PhaseRule
import net.lab0.skyscrapers.rule.RuleBook
import net.lab0.skyscrapers.rule.move.*
import net.lab0.skyscrapers.rule.move.build.*
import net.lab0.skyscrapers.rule.move.seal.BoardSealingContainmentRule
import net.lab0.skyscrapers.rule.move.seal.BuildersPreventsSealingRule
import net.lab0.skyscrapers.rule.move.seal.SealingRangeRule
import net.lab0.skyscrapers.rule.move.seal.SealsPreventSealingRule
import net.lab0.skyscrapers.rule.move.win.WinConditionRule
import net.lab0.skyscrapers.rule.placement.CantGiveUpDuringPlacementRule
import net.lab0.skyscrapers.rule.placement.PlaceBuilderOnEmptyCell
import net.lab0.skyscrapers.structure.Height

object Defaults {
  const val WIDTH = 5
  const val HEIGHT = 5
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
