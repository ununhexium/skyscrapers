package net.lab0.skyscrapers.client.clikt.struct

import kotlinx.serialization.Serializable

@Serializable
data class LastGame(val gameName:String, val token:String)
