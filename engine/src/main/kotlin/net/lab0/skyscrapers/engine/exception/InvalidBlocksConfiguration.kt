package net.lab0.skyscrapers.engine.exception

data class InvalidBlocksConfiguration(val reason: String) : InvalidConfiguration(reason)
