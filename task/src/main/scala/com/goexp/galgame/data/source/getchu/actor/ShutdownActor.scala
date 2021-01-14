package com.goexp.galgame.data.source.getchu.actor

import com.goexp.piplline.handler.DefaultActor

import java.util.concurrent.{Executors, TimeUnit}

class ShutdownActor(val delay: Long, val unit: TimeUnit) extends DefaultActor {

  val service = Executors.newSingleThreadScheduledExecutor()

  //shutdown after 1m
  val runner: Runnable = () => {
    logger.debug("Send shutdown message")

    this.sendShutdownMessage()
    service.shutdownNow()
  }

  private def delayRun() = {
    service.schedule(runner, delay, unit)
  }

  var f = delayRun()


  override def receive: Rec = {
    case "reset" =>
      f.cancel(true)
      logger.debug("Shutdown timer reset")

      f = delayRun()
  }
}