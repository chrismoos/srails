package com.cmoos.srails

import net.lag.logging.Logger

object Application {
  val log = Logger.get("Application")
  var routes: List[Map[String, Any]] = List()
  
  def addRoute(route: Map[String, Any]) {
    routes = routes ++ List(route)
  }
  
  def getRoutes(): List[Map[String, Any]] = {
    routes
  }
  
  def boot(obj: String) {
    log.info("Booting with object: " + obj)
    Class.forName(obj) match {
      case x => x.getMethod("boot").invoke()
    }
  }
}