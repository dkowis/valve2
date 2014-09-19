package org.protorepose.core.servlet

import javax.servlet.http.HttpServlet
import javax.servlet.{ServletConfig, ServletResponse, ServletRequest, Servlet}

import org.protorepose.core.CoreSpringProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.support.SpringBeanAutowiringSupport

class ReposeServlet extends HttpServlet {

  //TODO: somehow I need to get some info into here about the servlet we're going to run
  // How do I get ahold of a core context, which provides a way to get filters

  /**
   * Spring should magic this in during the init method, thanks to the SpringBeanAutowiringSupport.
   * Note this is just a context provider keeping the Core application context separate from the ones the filters
   * care about.
   * And I'm not sure I even need this at the servlet scope, since the filter will do it for me.
   */
  //@Autowired
  //val contextProvider:CoreSpringProvider = Nil

  override def service(req: ServletRequest, res: ServletResponse): Unit = {
    super.service(req, res)
  }

  override def init(): Unit = {
    super.init()
    //Manually trigger autowiring, doesn't work for constructors, has to be properties!
    //TODO: might not even need to do this: http://stackoverflow.com/a/2523288/423218
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this)

  }

}
