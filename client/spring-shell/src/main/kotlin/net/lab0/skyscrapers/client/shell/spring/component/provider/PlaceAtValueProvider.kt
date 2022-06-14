package net.lab0.skyscrapers.client.shell.spring.component.provider

import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.api.structure.Position.Style.COMA
import org.springframework.core.MethodParameter
import org.springframework.shell.CompletionContext
import org.springframework.shell.CompletionProposal
import org.springframework.shell.standard.ValueProvider
import org.springframework.stereotype.Component

@Component
class PlaceAtValueProvider: ValueProvider {
  override fun supports(
    parameter: MethodParameter?,
    completionContext: CompletionContext?
  ): Boolean {
    return true
  }

  override fun complete(
    parameter: MethodParameter,
    completionContext: CompletionContext,
    hints: Array<out String>?
  ): List<CompletionProposal> {
    return (0..4).flatMap { x->
      (0..4).map { y ->
        CompletionProposal(Position(x,y).toString(COMA))
      }
    }
  }
}