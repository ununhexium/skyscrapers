package net.lab0.skyscrapers.server.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.engine.api.BlocksData

@Serializable
data class BlocksDataDTO(val blocks: Map<Int, Int>) {
  constructor(blocks: BlocksData) : this(blocks.blocks.mapKeys { it.value })
}