package org.protorepose.valve.coreService

import org.springframework.stereotype.Component


@Component
trait CoreService {

  def coreServiceThingy(string:String):Unit
}
