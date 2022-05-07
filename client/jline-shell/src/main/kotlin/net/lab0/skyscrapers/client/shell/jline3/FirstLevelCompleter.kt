package net.lab0.skyscrapers.client.shell.jline3

import org.jline.reader.Candidate
import org.jline.reader.Completer
import org.jline.reader.LineReader
import org.jline.reader.ParsedLine

class FirstLevelCompleter: Completer {
  override fun complete(
    reader: LineReader?,
    line: ParsedLine?,
    candidates: MutableList<Candidate>
  ) {
    listOf(
      "new",
      "restart",
      "play",
    ).forEach {
      candidates.add(Candidate(it))
    }
  }
}