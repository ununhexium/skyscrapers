package net.lab0.skyscrapers.engine.exception

import net.lab0.skyscrapers.api.structure.GameRuleViolation

data class GameRuleViolationException(val violations: List<GameRuleViolation>) :
  Exception(
    """
      |${violations.size} rule${if(violations.size > 1) "s were" else " was"} violated:
      |${violations.joinToString(separator = "\n") { v ->
          "Name=${v.name}, Description=${v.description}, Detail=${v.detail}" 
        }
      }
    """.trimMargin()
  )
