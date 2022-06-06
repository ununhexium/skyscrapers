package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.structure.Bounds

@Serializable
data class BoundsDTO(
  val abscissaRange: IntRangeDTO,
  val ordinateRange: IntRangeDTO,
) {
  fun toModel(): Bounds =
    Bounds(abscissaRange.toModel(), ordinateRange.toModel())

  constructor(b: Bounds) :
      this(IntRangeDTO(b.abscissaRange), IntRangeDTO(b.ordinateRange))
}
