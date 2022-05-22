
rootProject.name = "skyscrapers"

include("ai")

include("api")
include("api:dto")
findProject(":api:dto")?.name = "dto"
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

include("engine")

include("server")
include("testing")
