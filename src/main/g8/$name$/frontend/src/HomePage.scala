package $name$.frontend

import com.raquo.laminar.api.L.*
import foxxy.frontend.utils.*
import $name$.shared.Endpoints
import zio.*

case class HomePage(httpClient: HttpClient) {
  import httpClient.extensions._
  def render = ZIO.attempt {
    div(
      text <-- Endpoints.todosEndpoint.send(()).map(_.toString()).toEventStream
    )
  }
}
