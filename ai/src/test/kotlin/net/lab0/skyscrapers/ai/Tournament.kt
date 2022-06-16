package net.lab0.skyscrapers.ai

interface Tournament<T> {
  fun compete(participants: Sequence<T>): T
}
