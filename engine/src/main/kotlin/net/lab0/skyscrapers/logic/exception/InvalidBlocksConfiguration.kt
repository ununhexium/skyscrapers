package net.lab0.skyscrapers.logic.exception

data class InvalidBlocksConfiguration(val reason: String) : InvalidConfiguration(reason)
