package com.cmoos.srails.servlet

import javax.servlet._
import javax.servlet.http._

import com.cmoos.demo._

class MainServlet extends HttpServlet {  
  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    Router.handleURI(req, resp)
  }
}