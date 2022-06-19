package net.lab0.skyscrapers.client.shell.spring

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import org.springframework.shell.CompletionProposal


fun haveCompletions(vararg expected: CompletionProposal) =
  haveCompletions(expected.toList())

fun haveCompletions(vararg expected: String) =
  haveCompletions(expected.map { CompletionProposal(it) })

fun haveTextCompletions(expected: List<String>) =
  haveCompletions(expected.map { CompletionProposal(it) })

fun haveCompletions(expected: List<CompletionProposal>) =
  Matcher<List<CompletionProposal>> { value ->
    val sameSize = value.size == expected.size

    if (sameSize) {
      val valid = value
        .mapIndexed { index, it -> index to it }
        .zip(expected)
        .map { (pair, expected) -> Triple(pair.first, pair.second, expected) }
        .groupBy { (_, actual, expected) ->
          actual.value() == expected.value() &&
              actual.displayText() == expected.displayText() &&
              actual.description() == expected.description() &&
              actual.category() == expected.category() &&
              actual.dontQuote() == expected.dontQuote()
        }

      val different = valid[false]
      val noDifference = different == null

      MatcherResult(
        sameSize && noDifference,
        {
          "Come completions didn't match:" +
              different!!.joinToString("\n\n") { (index, actual, expected) ->
                """
                    |At index $index expected:
                    |  ${expected.value()}
                    |  ${expected.displayText()}
                    |  ${expected.description()}
                    |  ${expected.category()}
                    |  ${expected.dontQuote()}
                    |But was:
                    |  ${actual.value()}
                    |  ${actual.displayText()}
                    |  ${actual.description()}
                    |  ${actual.category()}
                    |  ${actual.dontQuote()}
                  """
              }
        },
        { "Huhhh..." },
      )
    } else {
      MatcherResult(
        false,
        { "completions count was ${value.size} but we expected length ${expected.size}" },
        { "completions count was ${value.size} but we expected a length different from ${expected.size}" },
      )
    }
  }

