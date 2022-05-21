package net.lab0.skyscrapers.client.clikt.configuration

import kotlinx.serialization.Serializable

@Serializable
data class Global(
  val server: Server
)

