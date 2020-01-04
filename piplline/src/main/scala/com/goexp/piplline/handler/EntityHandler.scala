package com.goexp.piplline.handler

import com.goexp.piplline.core.Message
import com.typesafe.scalalogging.Logger

private[piplline]
trait EntityHandler {

  final private val logger = Logger(this.getClass)

  def handle(msg: Message): Unit = {
    val defaultCase: PartialFunction[Any, Unit] = {
      case x =>
        logger.error(s"No catch case!! Case:$x")
    }

    val func = processEntity orElse defaultCase
    func(msg.entity)
  }

  def processEntity: PartialFunction[Any, Unit]
}
