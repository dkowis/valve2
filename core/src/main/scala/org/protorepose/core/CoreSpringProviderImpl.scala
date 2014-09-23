package org.protorepose.core

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * In the real world, this guy would have to be configuration aware, so it can update/generate new spring contexts
 * as configuration is changed.
 *
 * Addition of new artifacts is easy, subtraction of artifacts is much much harder.
 *
 * Changing of what filters being used is also easy.
 *
 * We would probably have to do some kind of delta so that we can shut down contexts for filters that arent' being used
 * any longer.
 *
 * TODO: make this handle better core services being handled by the WAR container.
 */
object CoreSpringProviderImpl extends CoreSpringProvider with LazyLogging {

  lazy val coreContext = {
    val context = new AnnotationConfigApplicationContext()
    context.setDisplayName("CoreServiceContext")
    context.scan("org.protorepose.core.services")
    context.refresh()

    context
  }

  lazy val allServicesContext = {
    val context = new AnnotationConfigApplicationContext()
    context.setDisplayName("AllServicesContext")
    //If we set this directly, and we're in a war file, we will double the core services...
    //This could result in incorrect behavior, need to figure out how to resolve this
    context.setParent(coreServicesContext())
    context.scan("org.protorepose.services")
    context.refresh()

    context
  }

  lazy val filterContextMap: Map[String, ApplicationContext] = {
    //NOTE: somewhere around here we would have replaced this with a dynamic list of filters
    // So we can go annotation scan them on demand, not all the time

    //NOTE: this would be part of a config file or something
    val derpFilter = "org.protorepose.derpFilter.DerpFilter"

    val filterContext = new AnnotationConfigApplicationContext()
    filterContext.setParent(allServicesContext)
    filterContext.setDisplayName("DerpFilterContext")

    //Get the filter class itself
    val classLoader = this.getClass.getClassLoader
    val filterClass = classLoader.loadClass(derpFilter)

    val scanPackage = filterClass.getPackage.getName

    logger.info(s"scan package is |${scanPackage}|")

    filterContext.setParent(allServicesContext)
    filterContext.scan(scanPackage)

    filterContext.refresh()

    //NOTE, in the real world, we would do this for each configured filter
    Map(derpFilter -> filterContext)
  }

  /**
   * Get the application context containing only core services for filters
   * @return
   */
  override def coreServicesContext(): ApplicationContext = {
    coreContext
  }

  /**
   * Get the application context containing beans for services, either external or internal, for filters
   * @return
   */
  override def servicesContext(): ApplicationContext = {
    allServicesContext
  }

  /**
   * Get the application context containing the filter beans themselves?
   * @return
   */
  override def filtersContext(): Map[String, ApplicationContext] = {
    filterContextMap
  }

}
