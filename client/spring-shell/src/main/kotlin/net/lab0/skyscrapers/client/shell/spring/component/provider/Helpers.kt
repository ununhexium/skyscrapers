package net.lab0.skyscrapers.client.shell.spring.component.provider

import net.lab0.skyscrapers.api.structure.Position
import org.springframework.shell.CompletionContext


fun valueOfParam(completionContext: CompletionContext, paramName: String) =
  completionContext.words.let { words ->
    words.indexOf(paramName).let {
      if (it < 0) null else words.getOrNull(it + 1)
    }?.let {
      if (it.contains(",")) Position.fromComaStyle(it)
      else null
    }
  }
