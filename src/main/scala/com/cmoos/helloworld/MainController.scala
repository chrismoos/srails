package com.cmoos.helloworld

import com.cmoos.srails._

class MainController extends Controller {
  def index() {
    log.info("MainController: index")
    render_text("Hello, world!")
  }
}