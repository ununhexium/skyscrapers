package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class IntRangeDTO(val start: Int, val endExclusive: Int) {
  constructor(range:IntRange):
      this(range.start, range.endInclusive)

  fun toModel() =
    IntRange(start, endExclusive)
}
