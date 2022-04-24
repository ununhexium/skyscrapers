package net.lab0.skyscrapers

fun main() {
  val g = GameImpl(5, 5, 2, 1)

  println(g.phase)

  g.play {
    player(1) {
      placement {
        addBuilder(Position(0, 0))
      }
    }
  }
}
