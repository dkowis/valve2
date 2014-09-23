package org.protorepose.war

import java.util
import javax.servlet.{DispatcherType, ServletContext}

import org.protorepose.core.CoreSpringProviderImpl
import org.protorepose.core.servlet.{ReposeFilter, ReposeServlet}
import org.springframework.web.WebApplicationInitializer
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.filter.DelegatingFilterProxy

class ReposeInitializer extends WebApplicationInitializer {
  override def onStartup(container: ServletContext): Unit = {
    //Create our root app context!
    //TODO: THIS http://docs.spring.io/spring/docs/3.1.2.RELEASE/javadoc-api/org/springframework/web/WebApplicationInitializer.html

    val rootContext = new AnnotationConfigWebApplicationContext()
    rootContext.setParent(CoreSpringProviderImpl.allServicesContext)
    rootContext.setDisplayName("WARFileContext")

    rootContext.register(classOf[ReposeFilter])

    container.addListener(new ContextLoaderListener(rootContext))

    //Add repose servlet
    val registration = container.addServlet("reposeServlet", classOf[ReposeServlet])
    registration.addMapping("/*")

    //Add a repose filter, like you do
    val delegatingProxy = new DelegatingFilterProxy("reposeFilter")
    val filterRegistration = container.addFilter("springDelegatingFilterProxy", delegatingProxy)
    //I think I want this to be false... I don't fully understand this madness
    filterRegistration.addMappingForUrlPatterns(util.EnumSet.of(DispatcherType.REQUEST),false, "/*")
  }
}
