package net.lab0.skyscrapers.client.shell.jline3

import org.jline.reader.Candidate
import org.jline.reader.Completer
import org.jline.reader.LineReader
import org.jline.reader.ParsedLine

class NewGameCompleter : Completer {
  override fun complete(
    reader: LineReader?,
    line: ParsedLine?,
    candidates: MutableList<Candidate>
  ) {
    listOf(
      "--width",
      "--height",
      "--players",
      "--builders",
    ).forEach {
      candidates.add(Candidate(it))
    }
  }
}
