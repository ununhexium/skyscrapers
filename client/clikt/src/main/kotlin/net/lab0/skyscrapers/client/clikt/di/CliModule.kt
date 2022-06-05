package net.lab0.skyscrapers.client.clikt.di

import net.lab0.skyscrapers.client.clikt.configuration.Constants
import net.lab0.skyscrapers.client.http.SkyscraperClient
import net.lab0.skyscrapers.client.http.SkyscraperClientImpl
import org.koin.dsl.module


val cliModule = module {
  single<SkyscraperClient> { SkyscraperClientImpl(get()) }

  single(CONFIG_PATH) { Constants.configLocation }
}
