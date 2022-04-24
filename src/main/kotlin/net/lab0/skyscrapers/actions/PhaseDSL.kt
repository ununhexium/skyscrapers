package net.lab0.skyscrapers.actions

import net.lab0.skyscrapers.Position

class PhaseDSL(val player: Int) {
  fun placement(placement: PlacementPhaseDSL.() -> Action): Action = { game ->
    placement(PlacementPhaseDSL(this))(game)
  }

  fun building(building: BuildingPhaseDSL.() -> Action): Action = { game ->
    building(BuildingPhaseDSL(this))(game)
  }
}
