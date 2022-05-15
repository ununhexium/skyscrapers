package net.lab0.skyscrapers.server.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.logic.structure.Bounds

@Serializable
data class BoundsDTO(
  val width: Int,
  val height: Int
) {
  constructor(b: Bounds) : this(b.width, b.height)
}