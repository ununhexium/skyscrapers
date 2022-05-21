package net.lab0.skyscrapers.engine.api

import net.lab0.skyscrapers.engine.structure.Height

/**
 * Immutable list of blocks
 */
data class BlocksData(val blocks: Map<Height, Int>) {

  companion object {
    val EMPTY = BlocksData(mapOf())
  }

  constructor(vararg pairs: Pair<Height, Int>) : this(pairs.toList().toMap())

  fun isEmpty() =
    blocks.values.sumOf { it } == 0

  fun getBuildingBlocks() =
    blocks.filterKeys { it != Height.SEAL }

  fun removeBlockOfHeight(nextHeight: Height) =
    BlocksData(
      blocks.mapValues {
        if (it.key == nextHeight) it.value - 1 else it.value
      }
    )

  fun hasBlock(height: Height) =
    blocks.getOrDefault(height, 0) > 0

  fun removeSeal(): BlocksData =
    removeBlockOfHeight(Height.SEAL)

  fun maxHeight() =
    blocks.keys.maxByOrNull { it.value } ?: Height(0)

  fun toShortString() =
    blocks.entries.joinToString(separator = ", ") {
      "${it.key.value}:${it.value}"
    }
}