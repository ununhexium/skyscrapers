package net.lab0.skyscrapers.rule

import net.lab0.skyscrapers.api.Rule
import net.lab0.skyscrapers.api.Turn

fun <T> Rule<T>.dependsOn(rule:Rule<T>): CompositeDependencyRule<T> where T:Turn =
  CompositeDependencyRule(listOf(rule, this))
