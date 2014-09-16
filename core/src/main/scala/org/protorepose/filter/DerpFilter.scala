package org.protorepose.filter

import javax.servlet._

class DerpFilter extends Filter {
  override def init(filterConfig: FilterConfig): Unit = ???

  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit = ???

  override def destroy(): Unit = ???
}
