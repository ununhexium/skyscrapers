package net.lab0.skyscrapers.client.clikt.configuration

import kotlinx.serialization.Serializable

@Serializable
data class Server(
  val apiUrl: String,
)
