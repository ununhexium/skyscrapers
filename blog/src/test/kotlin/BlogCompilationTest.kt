import net.lab0.skyscrapers.blog.articles
import org.junit.jupiter.api.Test

class BlogCompilationTest {
  @Test
  fun `compile everything`() {
    articles.forEach { a ->
      a.page.build()
    }
  }
}
