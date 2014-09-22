package org.protorepose.core.servlet

import java.util.concurrent.atomic.AtomicReference
import javax.servlet._

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.protorepose.core.CoreSpringProviderImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.{ApplicationContextAware, ApplicationContext}
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import org.springframework.web.filter.DelegatingFilterProxy

/**
 * This component needs to be named, because we reference it in the web.xml using the delegating filter bean stuff
 * Remember this for wiring in scala http://stackoverflow.com/questions/17972896/autowired-on-a-constructor-of-a-scala-class
 * Note, this only needs to be a spring component if it's going to use some of the services, and we want them autowired
 * in. If we don't care about them being autowired in, we can use the SpringProvider thingy to get ahold of the contexts
 * that make sense and grab whatever we want out of them.
 */
@Component("reposeFilter")
class ReposeFilter @Autowired()(springEnv: Environment) extends DelegatingFilterProxy {

  @Autowired
  val appContext: ApplicationContext = null

  val filterReference = new AtomicReference[Filter]()

  /**
   * This is the servlet filter entrance for repose stuff.
   * This is where the start of doing any actual repose work happens.
   * We maybe have an existing FilterChain provided to us by the container, which we need to prepend to anything
   * repose generates.
   *
   * Then we need a new FilterChain instance containing the filters (or instances of singletons) we want to execute through
   * @param request
   * @param response
   * @param filterChain Any filter chain that may have preceded us. Could come from a bit of container magic.
   */
  override def doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain): Unit = {
    val portProp = springEnv.getProperty("port", "UNDEFINED")
    logger.info(s"Filtering in ${this} on port ${portProp}")


    //I don't think I actually need to do any madness here, possibly.

    val filter = filterReference.get()

    filter.doFilter(request, response, filterChain)
    logger.info("made call to derpfilter!")
  }

  //Replaces the init method on the filter, so that this guy can be spring aware
  override def initFilterBean(): Unit = {
    /**
     * This is where I would register for changes to the system model, or whatever
     * I would then build a new filter chain in that call back and atomically set it in the filterChainReference
     * Then new requests would go through that new filter
     *
     * Instead we're going to hard code a single filter, and call it
     */

    logger.info("ReposeFilter Bean init-ed")
    logger.info(s"What spring context are we in: ${this.appContext}")


    //Acquire the filter context and make calls
    val derpFilterContext:ApplicationContext = CoreSpringProviderImpl.filtersContext()("org.protorepose.derpFilter.DerpFilter")

    logger.info(s"entire filter context map: ${CoreSpringProviderImpl.filtersContext()}")
    logger.info("acquired derpfilter context")
    //Ignore the filter chain and just call the filter we have

    //This should get the bean of the filter we've got and execute it

    logger.info("Trying to get the bean using a bean by type!")
    val filter = derpFilterContext.getBean[Filter](classOf[Filter])

    filterReference.set(filter)

    //val filter = derpFilterContext.getBean("DerpFilter").asInstanceOf[Filter]
    logger.info("acquired filter bean")


    super.initFilterBean()
  }
}
