package net.lab0.skyscrapers.client.clikt

import net.lab0.skyscrapers.client.http.SkyscraperClient
import net.lab0.skyscrapers.client.http.SkyscraperClientImpl
import org.koin.dsl.module

val cliModule = module {
  single<SkyscraperClient> { SkyscraperClientImpl(get()) }
}
