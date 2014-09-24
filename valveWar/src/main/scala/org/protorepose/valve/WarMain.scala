package org.protorepose.valve

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.eclipse.jetty.annotations.AnnotationConfiguration
import org.eclipse.jetty.plus.webapp.{EnvConfiguration, PlusConfiguration}
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp._

object WarMain extends App with LazyLogging {

  val config = ConfigFactory.load()

  val banner =
    """
      |           WAR                             ██████╗
      |       Valve2: Electric Boogaloo           ╚════██╗
      |                                            █████╔╝
      |██╗   ██╗ █████╗ ██╗    ██╗   ██╗███████╗  ██╔═══╝
      |██║   ██║██╔══██╗██║    ██║   ██║██╔════╝  ███████╗
      |██║   ██║███████║██║    ██║   ██║█████╗    ╚══════╝
      |╚██╗ ██╔╝██╔══██║██║    ╚██╗ ██╔╝██╔══╝     Version: $myVersion
      | ╚████╔╝ ██║  ██║███████╗╚████╔╝ ███████╗     Jetty: $jettyVersion
      |  ╚═══╝  ╚═╝  ╚═╝╚══════╝ ╚═══╝  ╚══════╝
    """.stripMargin.
      replaceAll("\\$myVersion", config.getString("myVersion")).
      replaceAll("\\$jettyVersion", config.getString("jettyVersion"))

  //Create a couple jetties, and start stuff
  //The singleton that is the coreSpringProvider should keep things the way I want them to be
  println(banner)

  //we'll create two servers one on 8080 and one on 8081

  /**
   * So I could deploy programmatically a pair of war files, keeping the JVM stuff separate
   * Not sure how I didn't find this before, but it's super easy. (This could be within old servo)
   * Unfortunately, services are *not* shared amongst the war files, they seem to have their own memory hierarchy
   * I cannot figure out how to get the war file thingy to pick up my servlet 3.0 stuff :(
   * @param port
   * @return
   */
  def warServer(port: Int): Server = {
    val server = new Server(port)
    val webapp = new WebAppContext()
    webapp.setContextPath("/")
    webapp.setWar(config.getString("warLocation"))
    webapp.setConfigurations(Array(
      new AnnotationConfiguration(),
      new WebInfConfiguration(),
      new WebXmlConfiguration(),
      new MetaInfConfiguration(),
      new FragmentConfiguration(),
      new EnvConfiguration(),
      new PlusConfiguration(),
      new JettyWebXmlConfiguration()
    ))

    // Recommmend you set these up too.
    // webapp.setPersistTempDirectory(false)
    // webapp.setTempDirectory(pathToTempDirForYourApp)
    server.setHandler(webapp)
    server.start()

    logger.info(server.dump())

    server
  }

  logger.info("Starting up servers!")
  val server1 = warServer(8080)
  //val server2 = warServer(8081)

  logger.info("Joining to servers!")
  server1.join()
  //server2.join()
}
