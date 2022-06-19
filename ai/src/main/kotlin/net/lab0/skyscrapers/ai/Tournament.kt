package net.lab0.skyscrapers.ai

interface Tournament<T> {
  /**
   * @return Pair<Winner, Eliminated>.
   * The eliminated AIs are listed by order of alimination.
   */
  fun compete(participants: List<T>): Pair<T, List<T>>
}
