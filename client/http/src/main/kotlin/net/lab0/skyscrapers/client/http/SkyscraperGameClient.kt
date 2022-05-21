package net.lab0.skyscrapers.client.http

import net.lab0.skyscrapers.engine.api.GameState
import net.lab0.skyscrapers.engine.api.TurnType

interface SkyscraperGameClient {
  fun show(): GameState
  fun play(turn: TurnType)
}