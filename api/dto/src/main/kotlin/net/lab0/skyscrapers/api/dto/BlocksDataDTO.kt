package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.structure.BlocksData
import net.lab0.skyscrapers.api.structure.Height

@Serializable
data class BlocksDataDTO(val blocks: Map<Int, Int>) {
  fun toModel() = BlocksData(
    blocks.mapKeys { Height(it.key) }
  )

  constructor(blocks: BlocksData) : this(blocks.blocks.mapKeys { it.key.value })
}
