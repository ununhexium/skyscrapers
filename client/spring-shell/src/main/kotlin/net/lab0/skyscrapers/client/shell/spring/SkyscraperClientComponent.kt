package net.lab0.skyscrapers.client.shell.spring

import net.lab0.skyscrapers.client.http.SkyscraperClient
import net.lab0.skyscrapers.client.http.SkyscraperClientImpl
import org.http4k.core.HttpHandler
import org.springframework.stereotype.Component

@Component
class SkyscraperClientComponent(val handler: HttpHandler):
  SkyscraperClient by SkyscraperClientImpl(handler)
