package com.goexp.piplline.core

import java.util.concurrent.{ExecutorService, Executors, TimeUnit}

import com.goexp.piplline
import com.goexp.piplline.exception.RuntimeInterruptedException
import com.goexp.piplline.handler.HandlerConfig
import org.slf4j.LoggerFactory

import scala.collection.mutable

class Pipeline(private[this] val starter: Starter) {


  private val logger = LoggerFactory.getLogger(classOf[Pipeline])

  private val msgQueueProxy = new MessageQueueProxy[Message](1000)


  private val listenerExecutorService = Executors.newSingleThreadExecutor

  private val configs = mutable.Set[HandlerConfig]()

  private val defaultCase = {
    case x =>
      logger.error(s"No catch case!! Case:$x")
  }


  def registry(config: HandlerConfig): Pipeline = {
    configs.add(config)
    this
  }


  private def registry(handler: MessageHandler, executor: ExecutorService): Pipeline = {
    configs.add(piplline.handler.HandlerConfig(handler, executor))
    this
  }

  private def registry(handler: MessageHandler, threadCount: Int): Pipeline = {
    configs.add(piplline.handler.HandlerConfig(handler, Executors.newFixedThreadPool(threadCount)))
    this
  }


  def regForCPUType(handler: MessageHandler): Pipeline = {
    registry(handler, Runtime.getRuntime.availableProcessors)
  }

  def regForCPUType(handler: MessageHandler, threadCount: Int): Pipeline = {
    registry(handler, threadCount)
  }

  def regForCPUType(handler: MessageHandler, executor: ExecutorService): Pipeline = {
    registry(handler, executor)
  }

  def regForIOType(handler: MessageHandler): Pipeline = {
    registry(handler, 30)
  }

  def regForIOType(handler: MessageHandler, threadCount: Int): Pipeline = {
    registry(handler, threadCount)
  }

  def regForIOType(handler: MessageHandler, executor: ExecutorService): Pipeline = {
    registry(handler, executor)
  }


  def regGroup(group: Set[HandlerConfig]): Pipeline = {
    configs.addAll(group)
    this
  }

  def start(): Unit = {

    //fill queue
    starter.setQueue(msgQueueProxy)
    val mesTypeMap = configs.to(LazyList)
      .map { c =>
        c.handler.setQueue(msgQueueProxy)
        c
      }
      .groupBy {
        _.handler.getClass.hashCode()
      }
    //      .collect(Collectors.groupingBy(HandlerConfig.mesCode))
    //start message driven
    listenerExecutorService.execute { () =>
      var running = true
      while ( {
        running
      }) try {
        val mes = msgQueueProxy.poll(5, TimeUnit.MINUTES)
        if (mes != null) {
          mesTypeMap.get(mes.code) match {
            case Some(configs) =>
              for (c <- configs) {
                //exec actor
                c.executor.execute { () =>
                  try {
                    c.handler.process(mes)
                  }
                  catch {
                    case e: Exception =>
                      e.printStackTrace()
                  }
                }
              }
            case None =>
              logger.error(s"No message handler for: ${mes.code}")
          }
        }
        else {
          logger.info("listener task time out!!!")
          running = false

          //wait for all ok
          for (config <- configs) {
            config.executor.shutdown()
          }


          listenerExecutorService.shutdown()
        }
      } catch {
        case e: RuntimeInterruptedException =>
          e.printStackTrace()
          running = false
      }

    }


    starter.process()
  }
}

object Pipeline {

}
