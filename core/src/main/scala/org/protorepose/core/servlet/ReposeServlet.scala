package org.protorepose.core.servlet

import javax.servlet.{ServletConfig, ServletResponse, ServletRequest, Servlet}

class ReposeServlet extends Servlet {
  override def init(config: ServletConfig): Unit = ???

  override def getServletConfig: ServletConfig = ???

  override def getServletInfo: String = ???

  override def destroy(): Unit = ???

  override def service(req: ServletRequest, res: ServletResponse): Unit = ???
}
