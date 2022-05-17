package net.lab0.skyscrapers.logic.structure

@JvmInline
value class BuildersMatrix(val matrix: Matrix<Int?>) {
  companion object {
    fun from(input: String) =
      BuildersMatrix(
        Matrix.from(input) { if (it == ".") null else it.toInt() }
      )
  }
}
