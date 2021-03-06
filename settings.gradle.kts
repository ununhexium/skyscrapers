rootProject.name = "skyscrapers"

include("ai")

include("api")
include("api:dto")
findProject(":api:dto")?.name = "dto"
include("api:http4k")
findProject(":api:http4k")?.name = "http4k"
include("api:structure")
findProject(":api:structure")?.name = "structure"

include("blog")

include("client")
//include("client:jline-shell")
//findProject(":client:jline-shell")?.name = "jline-shell"
include("client:clikt")
findProject(":client:clikt")?.name = "clikt"
include("client:http")
findProject(":client:http")?.name = "http"
include("client:spring-shell")
findProject(":client:spring-shell")?.name = "spring-shell"

include("engine")

include("server")
include("testing")
