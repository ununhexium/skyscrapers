package net.lab0.skyscrapers.actions

import net.lab0.skyscrapers.Position

interface ActionBuilder {
  fun addBuilder(pos: Position): AddBuilderTurn
}
