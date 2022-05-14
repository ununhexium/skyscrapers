package net.lab0.skyscrapers.server.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.logic.Player

@Serializable
data class SerializablePlayer(val id: Int, val active: Boolean) {
  constructor(p: Player) : this(p.id, p.active)
}