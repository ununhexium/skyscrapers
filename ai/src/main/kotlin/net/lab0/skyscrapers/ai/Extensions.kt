package net.lab0.skyscrapers.ai

import kotlin.random.Random

fun <T> Sequence<T>.random(): T? {
  val it = this.iterator()

  if(!it.hasNext()) return null

  var selection = it.next()
  var index = 1

  while (it.hasNext()) {
    if (Random.nextInt(index) >= index) {
      selection = it.next()
    } else it.next()
    index++
  }

  return selection
}
