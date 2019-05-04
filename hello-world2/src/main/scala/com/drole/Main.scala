package com.drole

import java.util.UUID

import com.lambdaminute.slinkywrappers.reactrouter._

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportTopLevel, JSImport}
import scala.scalajs.LinkingInfo
import slinky.core._
import slinky.web.ReactDOM
import slinky.hot
import org.scalajs.dom
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._
import com.apollographql.scalajs._
import com.apollographql.scalajs.react.{ApolloProvider, Mutation, Query}
import scala.scalajs.js.{Array => JArray}

@JSImport("resources/index.css", JSImport.Default)
@js.native
object IndexCSS extends js.Object

object Main {
  val css = IndexCSS

  @JSExportTopLevel("main")
  def main(): Unit = {
    if (LinkingInfo.developmentMode) {
      hot.initialize()
    }

    val client = ApolloBoostClient(
      uri = "http://localhost:4466"
    )

    val container = Option(dom.document.getElementById("root")).getOrElse {
      val elem = dom.document.createElement("div")
      elem.id = "root"
      dom.document.body.appendChild(elem)
      elem
    }

    val reactComponent = div(BrowserRouter(withRouter(Demos)))

    ReactDOM.render(ApolloProvider(client)(reactComponent), container)
  }
}
@react class Demos extends Component {
  type Props = RouteProps

  case class State(path: String = "")

  override def initialState: State = State()

  def render(): ReactElement = {
    val home = "/"
    val hello1 = "/hello1"
    val hello2 = "/hello2"

    val component = div(
      Query[GetUsersQuery.Data](GetUsersQuery.operation) { queryStatus =>
        if (queryStatus.loading) "Loading..."
        else if (queryStatus.error.isDefined) s"Error! ${queryStatus.error}"
        else {
          val users = queryStatus.data match {
            case Some(value) =>
              value.users
                .map(_.get)
                .sortBy(_.age)
                .reverse
                .map { user =>
                  div(key := user.id)(
                    h1()(user.name),
                    p()(user.age),
                    p()(user.id)
                  )
                }
            case None =>
              JArray(div()("No users"))
          }
          div(
            users: _*
          )
        }
      })
    div(
      h1(s"WITHROUTER STUFF: ${props.location.pathname}"),
      ul(
        li(Link(to = home)("Home")),
        li(Link(to = hello1)("Hello1")),
        li(Link(to = hello2)("Hello2"))
      ),
      Route(exact = true, path = home, render = _ => h1("home!")),
      Route(exact = true, path = hello1, render = _ => HelloMessage("RUNNNNEE")),
      Route(exact = true, path = hello2, render = _ => component)
    )
  }

}
