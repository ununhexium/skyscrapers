package net.lab0.skyscrapers.server.dto

import kotlinx.serialization.Serializable

/**
 * @param player The user will play as this player
 * @param token The user must pass this token to play the game
 */
@Serializable
data class ConnectionResult(val player: Int, val token: String)
