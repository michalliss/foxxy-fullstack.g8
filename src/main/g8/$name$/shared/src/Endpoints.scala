package $name$.shared

import foxxy.shared.*
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.zio.*

object Endpoints {
  val helloEndpoint = BaseEndpoints.publicEndpoint.get.in("hello").out(jsonBody[String])
  val todosEndpoint = BaseEndpoints.publicEndpoint.get.in("todos").out(jsonBody[List[(String, String)]])
}
