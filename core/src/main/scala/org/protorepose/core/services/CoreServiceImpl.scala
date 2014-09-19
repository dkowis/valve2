package org.protorepose.core.services

class CoreServiceImpl extends CoreService {
  override def coreServiceThingy(string: String): Unit = {
    println(s"CORE SERVICE CALLED WITH $string")
  }
}
