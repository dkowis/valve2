package org.protorepose.core.services

import java.util.concurrent.atomic.AtomicInteger

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.springframework.stereotype.Component

@Component
class CoreServiceImpl extends CoreService with LazyLogging {

  val counter = new AtomicInteger(0)

  override def coreServiceThingy(string: String): Int = {
    val current = counter.incrementAndGet()
    logger.info(s"My instance is ${this} -- CALLED ${string}")
    logger.info(s"Counter: ${current}")

    current
  }
}
