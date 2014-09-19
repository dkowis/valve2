package org.protorepose.herpFilter

import javax.servlet._

import org.springframework.stereotype.Component

@Component("HerpFilter")
class HerpFilter extends Filter {
  override def init(filterConfig: FilterConfig): Unit = ???

  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit = ???

  override def destroy(): Unit = ???
}
