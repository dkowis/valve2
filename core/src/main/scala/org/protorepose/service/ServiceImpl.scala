package org.protorepose.service

class ServiceImpl extends Service{
  override def externalService(string: String): Unit = {
    println(s"EXTERNAL SERVICE CALL!!! $string")
  }
}
