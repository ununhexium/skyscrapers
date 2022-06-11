package net.lab0.skyscrapers.client.shell.spring.component

import net.lab0.skyscrapers.client.shell.spring.data.DarkPalette
import net.lab0.skyscrapers.client.shell.spring.data.Palette
import org.springframework.stereotype.Component

@Component
class DefaultPaletteComponent: Palette by DarkPalette
