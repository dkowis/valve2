package org.protorepose.core.services

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.springframework.stereotype.Component

@Component
class CoreServiceImpl extends CoreService with LazyLogging {
  override def coreServiceThingy(string: String): Unit = {
    logger.info("CORE SERVICE CALLED with {}", string)
  }
}
