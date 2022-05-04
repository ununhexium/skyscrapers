
rootProject.name = "skyscrapers"
include("logic")
include("ai")
include("blog")
include("client")
include("client:spring-shell")
findProject(":client:spring-shell")?.name = "spring-shell"
include("client:jline-shell")
findProject(":client:jline-shell")?.name = "jline-shell"
include("client:jexter-shell")
findProject(":client:jexter-shell")?.name = "jexter-shell"
include("client:jexter-shell")
findProject(":client:jexter-shell")?.name = "jexter-shell"
