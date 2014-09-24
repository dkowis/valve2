package org.protorepose.derpFilter

import javax.servlet._
import javax.servlet.http.HttpServletRequest

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.protorepose.core.services.CoreService
import org.protorepose.services.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component

@Component("DerpFilter")
class DerpFilter @Autowired()(service:Service, coreService:CoreService) extends Filter with LazyLogging {

  override def init(filterConfig: FilterConfig): Unit = {
    logger.debug("DerpFilter being initialized")
    //There's no config I need to do
  }

  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit = {
    val httpRequest = request.asInstanceOf[HttpServletRequest]
    logger.info(s"I'm in ur derp filter derp'in all the services: uri ${httpRequest.getRequestURI}")

    //This is a spring filter
    val counter = service.externalService("SERVICE got called!")
    val coreCounter = coreService.coreServiceThingy("DerpFilter")

    response.setContentType(MediaType.TEXT_PLAIN.toString)
    response.getWriter.write(counter.toString)
    response.getWriter.write(" from the external service!\n")
    response.getWriter.write(s"${coreCounter.toString} from the core service!")
    response.getWriter.flush()

    logger.info(s"Service is ${service}")
  }

  override def destroy(): Unit = {
    logger.debug("DerpFilter being destroyed")
    //meh
  }
}
