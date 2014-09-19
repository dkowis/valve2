package org.protorepose.core

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
trait CoreSpringProvider {

  /**
   * Get the application context containing only core services for filters
   * @return
   */
  def coreServicesContext():ApplicationContext

  /**
   * Get the application context containing beans for services, either external or internal, for filters
   * @return
   */
  def servicesContext():ApplicationContext

  /**
   * Get the application context containing the filter beans themselves?
   * @return
   */
  def filtersContext():Map[String, ApplicationContext]


}
