package net.lab0.skyscrapers.server.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.logic.structure.Matrix

@Serializable
data class MatrixDTO<T>(val data: List<List<T>>) {
  constructor(matrix: Matrix<T>) : this(matrix.data)
}