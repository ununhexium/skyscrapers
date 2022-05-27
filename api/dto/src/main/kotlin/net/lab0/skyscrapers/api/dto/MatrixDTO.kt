package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.structure.Matrix

@Serializable
data class MatrixDTO<T>(val data: List<List<T>>) {
  fun toModel() =
    Matrix(data)

  constructor(matrix: Matrix<T>) : this(matrix.data)
}