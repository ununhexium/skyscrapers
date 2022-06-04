package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.structure.Valued
import java.util.*

@Serializable
@JvmInline
value class AccessToken(override val value: String) : Valued<String> {
  companion object {
    fun random() = AccessToken(UUID.randomUUID().toString())
  }
}
