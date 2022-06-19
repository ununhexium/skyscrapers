package net.lab0.skyscrapers.client.shell.spring

import net.lab0.skyscrapers.client.FakeServerTest
import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.ServiceImpl
import net.lab0.skyscrapers.server.served
import org.http4k.server.PolyHandler
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class TestConfiguration : FakeServerTest {

  @Bean
  @Primary
  fun server(service: Service): PolyHandler =
    served(service)

  @Bean
  @Primary
  fun service(): Service =
    ServiceImpl(mutableMapOf())
}
