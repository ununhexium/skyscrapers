package net.lab0.skyscrapers.server.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.engine.api.BlocksData
import net.lab0.skyscrapers.engine.structure.Height

@Serializable
data class BlocksDataDTO(val blocks: Map<Int, Int>) {
  fun toBlocks() = BlocksData(
    blocks.mapKeys { Height(it.key) }
  )

  constructor(blocks: BlocksData) : this(blocks.blocks.mapKeys { it.value })
}
