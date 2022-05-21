package net.lab0.skyscrapers.server.dto

/**
 * @param player The user will play as this player
 * @param token The user must pass this token to play the game
 */
import kotlinx.serialization.Serializable

@Serializable
data class ConnectionResponse(val player: Int, val token: String)