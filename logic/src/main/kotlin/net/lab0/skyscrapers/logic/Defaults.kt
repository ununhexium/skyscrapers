package net.lab0.skyscrapers.logic

import net.lab0.skyscrapers.logic.api.BlocksData
import net.lab0.skyscrapers.logic.api.Move
import net.lab0.skyscrapers.logic.api.MoveAndTurn
import net.lab0.skyscrapers.logic.api.Rule
import net.lab0.skyscrapers.logic.rule.CheckCurrentPlayer
import net.lab0.skyscrapers.logic.rule.PhaseRule
import net.lab0.skyscrapers.logic.rule.RuleBook
import net.lab0.skyscrapers.logic.rule.move.BoardMoveContainmentRule
import net.lab0.skyscrapers.logic.rule.move.ClimbingRule
import net.lab0.skyscrapers.logic.rule.move.BuildersMoveToEmptyCells
import net.lab0.skyscrapers.logic.rule.move.MovementRangeRule
import net.lab0.skyscrapers.logic.rule.move.PlayersMoveTheirOwnBuilders
import net.lab0.skyscrapers.logic.rule.move.SealsPreventMovingRule
import net.lab0.skyscrapers.logic.rule.move.build.BlocksAvailabilityRule
import net.lab0.skyscrapers.logic.rule.move.build.BoardBuildingContainmentRule
import net.lab0.skyscrapers.logic.rule.move.build.BuildersPreventsBuildingRule
import net.lab0.skyscrapers.logic.rule.move.build.BuildingRangeRule
import net.lab0.skyscrapers.logic.rule.move.build.SealsPreventBuildingRule
import net.lab0.skyscrapers.logic.rule.move.seal.BoardSealingContainmentRule
import net.lab0.skyscrapers.logic.rule.move.seal.BuildersPreventsSealingRule
import net.lab0.skyscrapers.logic.rule.move.seal.SealingRangeRule
import net.lab0.skyscrapers.logic.rule.move.seal.SealsPreventSealingRule
import net.lab0.skyscrapers.logic.rule.move.win.WinConditionRule
import net.lab0.skyscrapers.logic.rule.placement.BoardPlacementContainmentRule
import net.lab0.skyscrapers.logic.rule.placement.CantGiveUpDuringPlacementRule
import net.lab0.skyscrapers.logic.rule.placement.PlaceBuilderOnEmptyCell
import net.lab0.skyscrapers.logic.structure.Bounds
import net.lab0.skyscrapers.logic.structure.Height

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

    moveRules = listOf<Rule<Move>>(
      BoardMoveContainmentRule,
      BuildersMoveToEmptyCells,
      MovementRangeRule(),
      ClimbingRule(),
      SealsPreventMovingRule,
    ),

    moveAndTurnRules = listOf<Rule<MoveAndTurn>>(
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
