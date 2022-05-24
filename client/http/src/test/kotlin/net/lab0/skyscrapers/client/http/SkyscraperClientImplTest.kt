package net.lab0.skyscrapers.client.http

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import net.lab0.skyscrapers.client.ServerIntegrationTest
import net.lab0.skyscrapers.server.ServiceImpl
import net.lab0.skyscrapers.server.value.GameName
import org.http4k.client.OkHttp
import org.http4k.core.Uri
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class SkyscraperClientImplTest : ServerIntegrationTest {

//  @Test
//  fun `can connect to a server`() {
//    useServer { client ->
//      val client = SkyscraperClientImpl(OkHttp(), url)
//      client.status().shouldBeRight()
//    }
//  }
//
//  @Test
//  fun `fail to connect to something that is not a game server`() {
//    // ssh port: nothing useful for us there
//    val client = SkyscraperClientImpl(OkHttp(), "http://localhost:22/api/")
//    client.status().shouldBeLeft()
//  }
//
//  @Disabled // TODO continue
//  @Test
//  fun `can show a game`() {
//    useServer { url ->
//      val client = SkyscraperClientImpl(OkHttp(), url)
//      val foo = GameName("foo")
//      client.create(foo).shouldBeRight()
//      client.join(foo).shouldBeRight()
//      val state = client.state(foo).shouldBeRight()
//      state shouldNotBe null
//    }
//  }
//
//  @Test
//  fun `can list games on the server`() {
//    useServer { url ->
//      val client = SkyscraperClientImpl(OkHttp(), url)
//      val list = client.listGames()
//      list shouldBe listOf()
//    }
//  }
//
//  @Test
//  fun `can create a game`() {
//    val service = ServiceImpl.new()
//
//    useServer(service = service) { url ->
//      val client = SkyscraperClientImpl(OkHttp(), url)
//      val gameName = "fus ro dah"
//      val fusRoDah = GameName(gameName)
//      client.create(fusRoDah).shouldBeRight()
//
//      client.listGames() shouldContain fusRoDah
//    }
//  }
//
//  @Test
//  fun `can't create a game with the same name`() {
//    val service = ServiceImpl.new()
//
//    useServer(service = service) { url ->
//      val client = SkyscraperClientImpl(OkHttp(), url)
//      val gameName = "fus ro dah"
//      val fusRoDah = GameName(gameName)
//
//      client.create(fusRoDah).shouldBeRight()
//      client.create(fusRoDah).shouldBeLeft()
//
//      client.listGames() shouldContain fusRoDah
//    }
//  }
}