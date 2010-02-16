package com.cmoos.srails

import freemarker.template._
import java.io.File
import java.io.OutputStreamWriter
import java.net.URLClassLoader
import freemarker.ext.beans._
import scala.collection.jcl.Conversions._
import org.scala_tools.javautils.Implicits._
import javax.servlet.http._
import net.lag.logging.Logger

import com.twitter.json.Json
import org.mortbay.jetty._

object TemplateRendering {
  val configuration: Configuration = new Configuration
  val classLoader: URLClassLoader = classOf[Controller].getClassLoader.asInstanceOf[URLClassLoader]
  configuration.setDirectoryForTemplateLoading(new File(classLoader.findResource("views/").getPath))
  configuration.setObjectWrapper(new DefaultObjectWrapper)
}


class ScalaMapModel(map: java.util.Map[_,_]) extends SimpleHash(map) {
  
}

class ScalaObjectWrapper extends ObjectWrapper {
  def wrap(obj: Object): TemplateModel = {
    new ScalaMapModel(obj.asInstanceOf[Map[String, Any]] asJava)
  }
}

class Controller {
  var request: Request = null
  var response: Response = null
  val log = Logger.get(this.getClass.getName)
  
  def setRequest(req: Request) {
    request = req
  }
  
  def setResponse(resp: Response) {
    response = resp
  }
  
  def getParam(key: String): Option[String] = {
    request.getParameter(key) match {
      case null => None
      case str => Some(str)
    }
  }
  
  def render_text(str: String) {
    response.getWriter.print(str)
  }
  
  def render_json(obj: Any) {
    response.getWriter.print(Json.build(obj).toString)
  }
  
  def render(templateName: String, model: Map[String, Any]) {
    val template = TemplateRendering.configuration.getTemplate(templateName)
    template.process(model, response.getWriter, new ScalaObjectWrapper)  }
}