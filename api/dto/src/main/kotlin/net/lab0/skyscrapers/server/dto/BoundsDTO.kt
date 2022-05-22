package net.lab0.skyscrapers.server.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.engine.structure.Bounds

@Serializable
data class BoundsDTO(
  val width: Int,
  val height: Int
) {
  fun toBounds(): Bounds  =
    Bounds(width, height)

  constructor(b: Bounds) : this(b.width, b.height)
}
