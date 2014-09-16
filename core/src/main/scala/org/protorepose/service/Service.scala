package org.protorepose.service

import org.springframework.stereotype.Component

@Component
trait Service {
  def externalService(string:String):Unit
}
