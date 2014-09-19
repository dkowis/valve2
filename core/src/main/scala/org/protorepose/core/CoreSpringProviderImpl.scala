package org.protorepose.core

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
 */
object CoreSpringProviderImpl extends CoreSpringProvider {

  lazy val coreContext = {
    val context = new AnnotationConfigApplicationContext()
    context.scan("org.protorepose.core.services")

    context
  }

  lazy val allServicesContext = {
    val context = new AnnotationConfigApplicationContext()
    context.setParent(coreServicesContext())
    context.scan("org.protorepose.services")

    context
  }

  lazy val filterContextMap:Map[String, ApplicationContext] = {
    //NOTE: somewhere around here we would have replaced this with a dynamic list of filters
    // So we can go annotation scan them on demand, not all the time

    //NOTE: this would be part of a config file or something
    val derpFilter = "org.protorepose.derpFilter.DerpFilter"

    val filterContext = new AnnotationConfigApplicationContext()
    filterContext.setParent(allServicesContext)

    //Get the filter class itself
    val classLoader = this.getClass.getClassLoader
    val filterClass = classLoader.loadClass(derpFilter)

    val scanPackage = filterClass.getPackage.toString


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
