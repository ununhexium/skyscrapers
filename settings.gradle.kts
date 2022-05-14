
rootProject.name = "skyscrapers"
include("logic")
include("ai")
include("blog")
include("client")
include("client:jline-shell")
findProject(":client:jline-shell")?.name = "jline-shell"
include("server")
