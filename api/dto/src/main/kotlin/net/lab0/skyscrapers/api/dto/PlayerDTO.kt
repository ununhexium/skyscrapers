package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.structure.Player

@Serializable
data class PlayerDTO(val id: Int, val active: Boolean) {
  constructor(p: Player) : this(p.id, p.active)

  fun toPlayer() =
    Player(id, active)
}