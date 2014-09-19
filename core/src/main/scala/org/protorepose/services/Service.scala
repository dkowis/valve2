package org.protorepose.services

import org.springframework.stereotype.Component

@Component
trait Service {
  def externalService(string:String):Unit
}
