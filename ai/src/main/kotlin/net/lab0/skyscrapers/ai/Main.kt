package net.lab0.skyscrapers.ai

fun main() {
  val arbiter = Arbiter(RandomAi(0), RandomAi(1))

  arbiter.playMatch(
    ConsoleStatePrinter,
    ConsoleTurnPrinter,
    ConsolePhaseObserver,
  )
}