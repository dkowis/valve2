package org.protorepose.valve

import java.util
import javax.servlet.DispatcherType

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{FilterHolder, ServletContextHandler}
import org.protorepose.core.CoreSpringProviderImpl
import org.protorepose.core.servlet.{ReposeFilter, ReposeServlet}
import org.springframework.core.env.MapPropertySource
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.filter.DelegatingFilterProxy

object Main extends App with LazyLogging {

  val config = ConfigFactory.load()

  val banner =
    """
      |                                           ██████╗
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
   * Create a servlet based jetty server that allows us to handle some spring contexts separately
   * This will configure the context loader listener to use our parent context so that there's a different filter instance
   * per server, but some of the contexts can be reused.
   *
   * This will reuse the filter beans even, between servers.
   * Only one instance of the services is turned on, and so this will ensure that services are only running once per local
   * machine.
   * @param port
   * @return
   */
  def servletServer(port: Int): Server = {
    val server = new Server(port)

    //This is the right kind of web application context, but something is missing -- the servletContext is null somehow
    val servletSpringContext = new AnnotationConfigWebApplicationContext()
    servletSpringContext.setParent(CoreSpringProviderImpl.allServicesContext)
    servletSpringContext.setDisplayName(s"ServletContext${port}") //Use the port to id the contexts

    //We only want to register our filter bean in this, it'll get everything else from the parent context
    servletSpringContext.register(classOf[ReposeFilter])

    //Set up some property sources, so we can send information to the spring context (port, clusterID, NodeID)?
    val propSources = servletSpringContext.getEnvironment.getPropertySources
    val props: Map[String, AnyRef] = Map("port" -> port.toString)

    import scala.collection.JavaConversions._
    val myProps = new MapPropertySource("dynamicNodeProps", props)

    //I think first is legit?
    propSources.addFirst(myProps)

    //Don't refresh the application context, the ContextLoaderListener will do it
    //servletSpringContext.refresh() //can't load the beans yet, because stuff isn't ready to go

    //Create a servletContextHandler to handle our ServletContext
    val contextHandler = new ServletContextHandler()
    contextHandler.setContextPath("/")
    contextHandler.addServlet(classOf[ReposeServlet], "/*") //Stick our servlet in there
    //Create a contextLoaderListener, for the
    val cll = new ContextLoaderListener(servletSpringContext)
    contextHandler.addEventListener(cll) //Add the contextLoaderListener to start things properly (I hope)

    //Create a filter holder to drop into the infrastructure
    val filterHolder = new FilterHolder()

    //Don't use our bean directly, but use the Delegating Filter Bean from spring, so that sanity happens
    // See: http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/filter/DelegatingFilterProxy.html
    //Create a spring delegating proxy for a repose filter bean
    val delegatingProxy = new DelegatingFilterProxy("reposeFilter")
    filterHolder.setFilter(delegatingProxy)
    filterHolder.setDisplayName("SpringDelegatingFilter")

    //I have no clue what dispatcher types I need :|
    //TODO: I don't have any freaking clue what this means.... Probably only want REQUEST, but I'm not sure
    contextHandler.addFilter(filterHolder, "/*", util.EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR))

    server.setHandler(contextHandler)

    server.start()
    server
  }

  logger.info("Starting up servers!")
  val server1 = servletServer(8080)
  val server2 = servletServer(8081)

  logger.info("Joining to servers!")
  server1.join()
  server2.join()
}
