package net.lab0.skyscrapers.exception

import net.lab0.skyscrapers.api.GameRuleViolation

data class GameRuleViolationException(val violation: List<GameRuleViolation>) :
  Exception(
    """
      |${violation.size} rule${if(violation.size > 1) "s were" else " was"} violated:
      |${violation.joinToString(separator = "\n") { v ->
          "Name=${v.name}, Description=${v.description}, Detail=${v.detail}" 
        }
      }
    """.trimMargin()
  )
