package $name$.backend

import foxxy.repo.*
import io.getquill.*
import io.getquill.jdbczio.Quill
import zio.*

import java.util.UUID

case class Schema(quill: Quill.Postgres[SnakeCase]) {
  import quill.*

  inline def users     = createEntity[Schema.UserDB]("users")
  inline def todoItems = createEntity[Schema.TodoItemDB]("todo_items")
}

object Schema {
  case class UserDB(id: UUID, name: String)
  case class TodoItemDB(id: UUID, user_id: UUID, text: String, completed: Boolean)

  def live = ZLayer.derive[Schema]
}

case class Repository(schema: Schema) {
  import schema.quill.*

  def allTodos                                 = schema.quill.run(schema.todoItems)
  def addUser(user: Schema.UserDB)             = schema.quill.run(schema.users.insertValue(lift(user)).returning(x => x))
  def addTodoItem(todoItem: Schema.TodoItemDB) = schema.quill.run(schema.todoItems.insertValue(lift(todoItem)).returning(x => x))
}

object Repository {
  def live = ZLayer.derive[Repository]
}
