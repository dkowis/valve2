package org.protorepose.derpFilter

import javax.servlet._

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.protorepose.services.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component("DerpFilter")
class DerpFilter @Autowired()(service:Service) extends Filter with LazyLogging {

  override def init(filterConfig: FilterConfig): Unit = {
    logger.debug("DerpFilter being initialized")
    //There's no config I need to do
  }

  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit = {
    logger.info("I'm in ur derp filter derp'in all the services")
    //This is a spring filter
    service.externalService("SERVICE got called!")
    logger.info(s"Service is ${service}")
  }

  override def destroy(): Unit = {
    logger.debug("DerpFilter being destroyed")
    //meh
  }
}
