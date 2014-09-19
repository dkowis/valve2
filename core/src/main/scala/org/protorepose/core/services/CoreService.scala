package org.protorepose.core.services

import org.springframework.stereotype.Component


@Component
trait CoreService {

  def coreServiceThingy(string:String):Unit
}
