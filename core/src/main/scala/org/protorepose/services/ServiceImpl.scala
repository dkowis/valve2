package org.protorepose.services

import java.util.concurrent.atomic.AtomicInteger

import com.typesafe.scalalogging.slf4j.LazyLogging

class ServiceImpl extends Service with LazyLogging {

  val counter:AtomicInteger = new AtomicInteger(0)

  override def externalService(string: String): Integer = {
    logger.info(s"External service got called with $string")
    val value = counter.incrementAndGet()

    logger.info(s"Counter: $value")

    value
  }
}
