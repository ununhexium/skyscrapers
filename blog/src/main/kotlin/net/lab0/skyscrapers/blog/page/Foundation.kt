package net.lab0.skyscrapers.blog.page

import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.li
import kotlinx.html.ol
import kotlinx.html.p
import kotlinx.html.pre
import kotlinx.html.ul
import net.lab0.skyscrapers.blog.engine.KotlinxHtmlRawBody
import net.lab0.skyscrapers.blog.engine.PageImpl
import net.lab0.skyscrapers.blog.engine.Publication
import net.lab0.skyscrapers.blog.engine.html.Pointer
import net.lab0.skyscrapers.blog.engine.html.github
import net.lab0.skyscrapers.blog.engine.html.googleSearchImageLink
import net.lab0.skyscrapers.blog.engine.html.image
import net.lab0.skyscrapers.blog.engine.html.mono

object Foundation : Publication {
  override val id = "5/8/22 6:39 PM"
  override val after = asInstant("5/8/22 6:39 PM")
  override val lastUpdate = asInstant("5/8/22 6:39 PM")

  override val page = object : PageImpl() {
    override val title = "Foundation"

    override val body = KotlinxHtmlRawBody {
      h1 { +"Foundation" }

      p {
        +"""
              |The foundation of the game is the logic: placement, movement, rules, ...
              |
              |The whole development process is test driven as it's both very 
              |easy to accidentally breakma rule and a set of rules is easily
              |transformed into a set of tests.
              | 
              |The game was developed in the order in which the player would play it.
            """.trimMargin()

        ol {
          li { github(Pointer.Tag("Initialization", "_blog_init")) }
          li { github(Pointer.Tag("Turns", "_blog_turns")) }
          li { +"Builders' placement" }
          li { +"Movement" }
          li { github(Pointer.Tag("Building", "_blog_moveandbuild")) }
          li { +"Sealing" }
          li { +"Building blocks" }
          li { +"Building rules" }
        }
      }

      h2 { +"First flaws" }

      p {
        +"""
            |So far so good. A few growing pains start to appear though.
          """.trimMargin()

        ul {
          li {
            +"The game is a 2D board but arrays are still used to access it. There array don't play nicely with the "
            pre { +"Position" }
            +" class"
          }
          li { +"The game implementation test file is growing large and it's still far from done" }
          li { +"The game rules are all implemented in a single method and the rules can't be composed and shared" }
          li { +"The are a lot of exception only to manage breaking the game's rules" }
        }
      }

      h2 { +"Matrix" }

      p {
        +"""
            |The matrix issue looks like the smallest investment do to for quite
            |a large benefit (abstraction of coordinates manipulation).
            |
            |This will be reused a lot later, not only for the game's logic,
            |but also the AI and the serialization.
          """.trimMargin()

        ul {
          li { github(Pointer.Tag("Matrix Class", "_blog_enter_the_matrix")) }
        }
      }

      h2 { +"Rules" }

      p {
        +"""
          |Now the rules' implementation need to be refactored to be modular.
          |
          |There's the option to add booleans to enable/disable rules but this causes troubles:
        """.trimMargin()

        ul {
          li { +"Readability" }
          li { +"Expressiveness" }
          li { +"User information" }
        }

        p {
          +"Booleans are not expressive and sending a list of booleans like "
          pre {
            +"setRules(true, false, true, true, false, null)"
          }
          +"is barely readable."

          +"""
            |It's harder to configure or alter a rule to customize it and
            | create new modes.
            |Instead of managing a like of boolean, there would not be a mix
            | of booleans, integers (movement range), functions (for powerups), ...
          """.trimMargin()

          +"""
            |Informing the user about which rule was broken and why would need
            |to happen in the function which evaluates the rules.
            |This will make the function bigger (and eventually unmanageable) 
            |as in addition to evaluating the rules it would also need to tell
            |the user 
            |which rule was broken,
            |which part of the movement broke it,
            |where the problem happened on the board,
            |then maybe also translate that for different languages.
          """.trimMargin()

          """
            |It's time to introduce a new concept to separate these concerns.
          """.trimMargin()

          ul {
            li {
              github(
                Pointer.Tag(
                  "Add the concept of rules",
                  "_blog_first_rules"
                )
              )
            }
            li {
              github(
                Pointer.Tag(
                  "Finished with the transitions to rules-based evaluation instead of a single function",
                  "_blog_migrated_to_rules"
                )
              )
            }
          }
        }

        h2 { +"State" }

        p {
          +"""
            |The next problem is the game's state.
            |The GameImpl is the only state that exists.
            |Most importantly
          """.trimMargin()

          ul {
            li { +"There is no history (can't replay a game)" }
            li { +"The state is mutable and this may cause trouble when it will be more complex" }
            li {
              +"There is "
              github(
                Pointer.Line(
                  "already a place",
                  "_blog_migrated_to_rules",
                  "src/main/kotlin",
                  "net/lab0/skyscrapers/GameImpl.kt",
                  149
                )
              )
              +"""
                |where the state is partially mutated.
              """.trimMargin()
            }
          }

          +"""
            |Having the game use immutable states and only implementing 
            |transformations between these states solves these issues.
            |
            |In addition, the state transformations can be used in the AI
            |to compute future states and check rules on them.
          """.trimMargin()

          p {
            +"The game now has "

            github(
              Pointer.Tag(
                "immutable states and a history",
                "_blog_use_immutable_state"
              )
            )

            +"."
          }
        }

        h2 { +"Modularization" }

        p {
          +"""
            |The game logic is now good enough to stop the development and 
            |continue with the other parts of the game.
            |To do that, the code developed so far is put in a module (
          """.trimMargin()

          mono("logic")

          +"""
            |)
          """.trimMargin()

          p {
            github(
              Pointer.Tag(
                "'Last' commit",
                "_blog_prepare_modularization"
              )
            )
            +" for the game's logic"
          }
        }

        image("foundation/foundation.jpg")
      }
    }
  }
}
