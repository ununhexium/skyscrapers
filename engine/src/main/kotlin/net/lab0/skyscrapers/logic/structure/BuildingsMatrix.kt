package net.lab0.skyscrapers.logic.structure

@JvmInline
value class BuildingsMatrix(val matrix: Matrix<Int>) {
  companion object {
    fun from(input: String) =
      BuildingsMatrix(
        Matrix.from(input) { it.toInt() }
      )
  }
}
