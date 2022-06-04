package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class ConnectionResponse(val player: Int, val token: AccessToken)
