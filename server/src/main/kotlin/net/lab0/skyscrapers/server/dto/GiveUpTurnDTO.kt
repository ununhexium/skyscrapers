package net.lab0.skyscrapers.server.dto

import kotlinx.serialization.Serializable

@Serializable
data class GiveUpTurnDTO(val player: Int)