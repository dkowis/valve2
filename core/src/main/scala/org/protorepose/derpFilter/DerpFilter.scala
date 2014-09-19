package org.protorepose.derpFilter

import javax.servlet._

import org.protorepose.services.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component("DerpFilter")
class DerpFilter @Autowired()(service:Service) extends Filter {
  override def init(filterConfig: FilterConfig): Unit = {
    //There's no config I need to do
  }

  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit = {
    //This is a spring filter
    service.externalService("SERVICE got called!")
  }

  override def destroy(): Unit = {
    //meh
  }
}
