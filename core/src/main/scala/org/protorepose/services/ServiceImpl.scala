package org.protorepose.services

class ServiceImpl extends Service{
  override def externalService(string: String): Unit = {
    println(s"EXTERNAL SERVICE CALL!!! $string")
  }
}
