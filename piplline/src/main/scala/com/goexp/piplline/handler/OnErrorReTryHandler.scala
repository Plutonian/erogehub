package com.goexp.piplline.handler

import java.util.Objects._
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

import com.goexp.piplline.core.{Message, MessageHandler, Pipeline, Starter}
import com.goexp.piplline.handler.OnErrorReTryHandler._
import org.slf4j.LoggerFactory

import scala.collection.concurrent.TrieMap

abstract class OnErrorReTryHandler(private[this] val retryTimes: Int) extends MessageHandler {
  require(retryTimes > 0, "times must > 0")

  var waitTime: Int = _
  var unit: TimeUnit = _

  def this(retryTimes: Int, waitTime: Int, unit: TimeUnit) {
    this(retryTimes)

    requireNonNull(unit)

    this.waitTime = waitTime
    this.unit = unit
  }


  final private val logger = LoggerFactory.getLogger(classOf[OnErrorReTryHandler])

  override def process(message: Message): Unit = {

    message.entity match {
      case entity =>
        try {
          action(message)
        }
        catch {
          case e: Exception =>
            val timesCounter = getCounter(entity)

            val errorTimes = timesCounter.incrementAndGet()
            if (errorTimes <= retryTimes) {

              logger.error(s"[Retry times:${errorTimes}] Entry:${entity}")

              //sleep current thread
              if (waitTime > 0)
                unit.sleep(waitTime)

              sendTo(getClass, entity)
            } else {
              logger.error(s"Out of retry times! Retry times:$retryTimes Entry:${entity} ")
            }

            throw e
        }
    }
  }


  def action(message: Message): Unit

}

private object OnErrorReTryHandler {
  private val map = new TrieMap[Any, AtomicInteger]()

  private def getCounter(entity: Any) =
    map.get(entity) match {
      case None =>
        val counter = new AtomicInteger(0)
        map.put(entity, counter)
        counter
      case Some(counter) => counter
    }

}

object Tester {
  def main(args: Array[String]): Unit = {
    new Pipeline(TestStart)
      .regForCPUType(TestHandler)
      .start()
  }

  object TestStart extends Starter {
    override def process(): Unit = {
      Range(0, 10)
        .foreach {
          i =>
            sendTo(TestHandler.getClass, i)
        }
    }
  }

  object TestHandler extends OnErrorReTryHandler(3, 1, TimeUnit.SECONDS) {

    var count_1: Int = _
    var count_3: Int = _

    override def action(message: Message): Unit = {
      message.entity match {
        case 1 =>
          count_1 += 1

          if (count_1 != 3)
            throw new Exception("Error")
        case 3 =>
          count_3 += 1

          if (count_3 != 5)
            throw new Exception("Error")
        case _ =>
      }
    }
  }

}


