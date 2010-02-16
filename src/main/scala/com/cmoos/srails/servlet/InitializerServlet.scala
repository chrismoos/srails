package com.cmoos.srails.servlet

import javax.servlet._
import javax.servlet.http._
import java.net.URLClassLoader

import net.lag.configgy._
import net.lag.logging.Logger

import com.cmoos.srails.Application

class InitializerServlet extends HttpServlet {
  val log = Logger.get("Initializer")
  val classLoader: URLClassLoader = classOf[Controller].getClassLoader.asInstanceOf[URLClassLoader]
  
  override def init() {
    log.info("Starting boot...")
    loadRoutes
    loadBoot
  }
  
  private def loadBoot() {
    Configgy.configure(classLoader.findResource("config/boot.conf").getPath)
    val config = Configgy.config
    config.getConfigMap("boot") match {
      case Some(boot: ConfigMap) => boot.getString("object") match {
        case Some(obj: String) => Application.boot(obj)
        case None => 
      }
      case None => 
    }
  }
  
  private def loadRoutes() {
    Configgy.configure(classLoader.findResource("config/routes.conf").getPath)
    val config = Configgy.config
    var routesAdded = 0
    config.getConfigMap("routes") match {
     case Some(routes: ConfigMap) => {
       routes.keys.foreach(key => {
         processRoute(routes.getConfigMap(key)) match {
           case true => routesAdded = routesAdded + 1
           case false => 
         }
       })
       log.info("Added " + routesAdded.toString + " routes.")
     }
     case _ => log.info("No routes found")
    }
  }
  
  private def processRoute(route: Option[ConfigMap]): Boolean = route match {
    case Some(routeConfig: ConfigMap) => List(routeConfig.getString("path"), routeConfig.getString("controller"), routeConfig.getString("action")) match {
      case List(Some(path: String), Some(controller: String), Some(action: String)) => {
        Application.addRoute(Map("controller" -> controller, "path" -> path, "action" -> action))
        true
      }
      case params => {
        log.error("Invalid route parameters: " + params.toString)
        false
      }
    }
    case None => {
      log.error("Invalid route: " + route.toString)
      false
    }
  }
}