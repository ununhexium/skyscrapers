package net.lab0.skyscrapers.exception

data class InvalidBlocksConfiguration(val reason: String) : InvalidConfiguration(reason)
