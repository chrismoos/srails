import sbt._

class SRailsProject(info: ProjectInfo) extends DefaultWebProject(info) {
  val jetty6 = "org.mortbay.jetty" % "jetty" % "6.1.14" % "compile, test"
  val servlet = "javax.servlet" % "servlet-api" % "2.4" % "compile, test"
  val freemarker = "org.freemarker" % "freemarker" % "2.3.16" % "compile"
  val javaTools = "org.scala-tools" % "javautils" % "2.7.4-0.1" % "compile"
  val commons = "commons-lang" % "commons-lang" % "2.2" % "compile"
  val commons_codec = "commons-codec" % "commons-codec" % "1.4" % "compile"
}