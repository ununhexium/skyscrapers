package net.lab0.skyscrapers.blog.page

import kotlinx.html.li
import kotlinx.html.p
import kotlinx.html.ul
import net.lab0.skyscrapers.blog.engine.KotlinxHtmlRawBody
import net.lab0.skyscrapers.blog.engine.PageImpl
import net.lab0.skyscrapers.blog.engine.Publication
import net.lab0.skyscrapers.blog.engine.html.googleSearchImageLink
import net.lab0.skyscrapers.blog.engine.html.image

object GameDesign : Publication {
  override val id = "5/8/22 1:19 PM"
  override val after = asInstant("5/8/22 1:19 PM")
  override val lastUpdate = asInstant("5/8/22 1:19 PM")

  override val page = object : PageImpl() {
    override val title = "Game Design"

    override val body = KotlinxHtmlRawBody {
      p {
        +"""
          This is the first time that I want to build a game that has all of the following traits:
        """.trimIndent()
      }

      ul {
        li { +"Multiplayer" }
        li { +"Online" }
        li { +"Has an AI" }
        li { +"Has a CLI client" }
        li { +"Has a web client" }
        li { +"Has a server to connect players" }
      }

      p {
        +"""
          |The focus will be on simplicity.
          |The game will consist in staking cubes (floors) to make towers (skyscrapers) and reach the highest level possible.
          |This is like the
        """.trimMargin()
        googleSearchImageLink("Santorini game")
        +"""
          |but I'd like it to be bigger and customizable and benefit from usual .
         """.trimMargin()
      }

      p {
        +"""
          |So here is what I'm aiming for: 
        """.trimMargin()

        ul {
          li { +"The game can be played by 2 or more players" }
          li { +"The objective of the game is to be the first player to reach the maximum level skyscraper" }
          li { +"The player plays via a builder" }
          li { +"The builder can climb and fall from the buildings" }
          li { +"The players have a limited set of blocks that they can use" }
          li { +"The players have to option to seal a building to prevent further construction" }
        }
      }

      image("game_design/preview.jpg")

      p {
        +"""
          The players have 4 play options:
        """.trimIndent()

        ul {
          li { +"Place a builder" }
          li { +"Build" }
          li { +"Move a builder" }
          li { +"Seal a location" }
        }
      }

      image("game_design/moves.jpg")
    }
  }
}