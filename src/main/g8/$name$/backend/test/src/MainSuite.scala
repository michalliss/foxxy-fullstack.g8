package $name$.backend

import foxxy.backend.*
import foxxy.testing.*
import zio.*
import zio.test.*
import $name$.shared.Endpoints

object MainSuite extends ZIOSpecDefault {
  val spec = suite("Simple end to end test")(
    test("Hello endpoint returns \"Hello, World!\"") {
      for {
        response <- TestClient.send(Endpoints.helloEndpoint, ())
      } yield assert(response)(Assertion.isRight(Assertion.equalTo("Hello, World!")))
    },
    test("Todo endpoint returns list of todo items") {
      for {
        response <- TestClient.send(Endpoints.todosEndpoint, ())
      } yield assert(response)(
        Assertion.isRight(
          Assertion.equalTo(
            List(
              ("Alice's first todo", "false"),
              ("Alice's second todo", "false"),
              ("Bob's first todo", "false"),
              ("Bob's second todo", "false")
            )
          )
        )
      )
    }
  ).provide(
    TestClient.startOnFreePort(
      port => Main.configurableLogic.provide(BackendConfig.withPort(port), PostgresContainer.layer),
      client => client.send(Endpoints.helloEndpoint, ()).unit
    )
  )
    @@ TestAspect.withLiveClock @@ TestAspect.silentLogging
}
