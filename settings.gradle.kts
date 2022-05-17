
rootProject.name = "skyscrapers"
include("engine")
include("ai")
include("blog")
include("client")
include("client:jline-shell")
findProject(":client:jline-shell")?.name = "jline-shell"
include("server")
include("api")
include("client:clikt")
findProject(":client:clikt")?.name = "clikt"
