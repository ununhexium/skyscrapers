package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@JvmInline
value class AccessToken(val value: String) {
  companion object {
    val EMPTY = AccessToken("")

    fun random() = AccessToken(UUID.randomUUID().toString())
  }
}
