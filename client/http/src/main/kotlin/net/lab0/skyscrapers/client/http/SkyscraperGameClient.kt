package net.lab0.skyscrapers.client.http

import arrow.core.Either
import net.lab0.skyscrapers.engine.api.GameState
import net.lab0.skyscrapers.engine.api.TurnType

interface SkyscraperGameClient {
  fun state(): Either<Errors, GameState>
  fun play(turn: TurnType): Either<Errors, GameState>
}
