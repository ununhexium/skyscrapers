package net.lab0.skyscrapers.client.shell.spring.data

import net.lab0.skyscrapers.api.structure.Height

data class Cell(val height: Height, val sealed: Boolean, val player: Int?)
