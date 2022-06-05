package net.lab0.skyscrapers.api.structure

interface MoveOnly {
  companion object {
    fun make(
      start: Position,
      target: Position,
    ) = object: MoveOnly {
      override val start = start
      override val target = target
    }
  }

  val start: Position
  val target: Position
}