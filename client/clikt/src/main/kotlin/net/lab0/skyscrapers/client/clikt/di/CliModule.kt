package net.lab0.skyscrapers.client.clikt.di

import net.lab0.skyscrapers.client.clikt.configuration.Constants
import net.lab0.skyscrapers.client.http.SkyscraperClient
import net.lab0.skyscrapers.client.http.SkyscraperClientImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.nio.file.Path



val cliModule = module {
  single<SkyscraperClient> { SkyscraperClientImpl(get()) }

  single(qualifier = CONFIG_PATH) { Constants.configLocation }
}

val CONFIG_PATH = named("configPath")