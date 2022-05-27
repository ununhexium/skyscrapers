package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.structure.Bounds

@Serializable
data class BoundsDTO(
  val width: Int,
  val height: Int
) {
  fun toModel(): Bounds =
    Bounds(width, height)

  constructor(b: Bounds) : this(b.width, b.height)
}