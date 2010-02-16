package com.cmoos.srails

import scala.util.matching.Regex
import java.io._
import javax.servlet.http._

import org.mortbay.jetty._

object Router {  
  private def matchURL(url: String, routes: List[Any]): Option[Map[String, Any]] = routes match {
    case Nil => None
    case (route: Map[String, Any]) :: tail => {
      route.get("path") match {
        case Some(path: String) => path.r.findFirstMatchIn(url) match {
          case Some(found) => {
            Some(route)
          }
          case None => matchURL(url, tail)
        }
        case None => matchURL(url, tail)
      }
    }
  }
  
  def processActionFilters(annotations: Array[java.lang.annotation.Annotation]) {
    /*annotations.foreach(annotation => annotation match {
      //case authorize: Authorize => println(authorize.roles.toString)
    })*/
  }

  def dispatchController(request: HttpServletRequest, response: HttpServletResponse, route: Map[String, Any]) {
    List(route.get("controller"), route.get("action")) match {
      case List(Some(controller: String), Some(action: String)) => {
        Class.forName(controller) match {
          case controllerClass: Class[Controller] => {
            val controllerInstance = controllerClass.newInstance
            controllerInstance.setRequest(request.asInstanceOf[Request])
            controllerInstance.setResponse(response.asInstanceOf[Response])
            
            val method = controllerClass.getMethod(action)

            method.invoke(controllerInstance)
          }
        }
      }
      case _ => println("Invalid route: " + route.toString)
    }
  }
  
  def handleURI(request: HttpServletRequest, response: HttpServletResponse) {
    matchURL(request.getPathInfo, Application.getRoutes) match {
      case Some(route) => {
        dispatchController(request, response, route)
      }
      case None => {
        response.getWriter().print("404 Not Found")
      }
    }
  }
}