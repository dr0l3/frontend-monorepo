package com.drole

import com.lambdaminute.slinkywrappers.reactrouter.RouteProps
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html.div



@react class HelloMessage extends StatelessComponent {
  case class Props(name: String)


  override def render(): ReactElement = {
    div("Hello ,", props.name)
  }
}
