package com.goexp.galgame.data.piplline.core

import java.util.concurrent.{ExecutorService, Executors, TimeUnit}

import com.goexp.galgame.data.piplline.exception.RuntimeInterruptedException
import com.goexp.galgame.data.piplline.handler.HandlerConfig
import org.slf4j.LoggerFactory

import scala.collection.mutable

class Pipeline(private[this] val starter: Starter) {


  private val logger = LoggerFactory.getLogger(classOf[Pipeline])

  private val msgQueueProxy = new MessageQueueProxy[Message](1000)


  private val listenerExecutorService = Executors.newSingleThreadExecutor

  private val configs = mutable.Set[HandlerConfig]()


  def registry(config: HandlerConfig): Pipeline = {
    configs.add(config)
    return this
  }


  private def registry(handler: MessageHandler, executor: ExecutorService): Pipeline = {
    configs.add(HandlerConfig(handler, executor))
    return this
  }

  private def registry(handler: MessageHandler, threadCount: Int): Pipeline = {
    configs.add(new HandlerConfig(handler, Executors.newFixedThreadPool(threadCount)))
    return this
  }


  def regForCPUType(handler: MessageHandler): Pipeline = {
    return registry(handler, Runtime.getRuntime.availableProcessors)
  }

  def regForCPUType(handler: MessageHandler, threadCount: Int): Pipeline = {
    return registry(handler, threadCount)
  }

  def regForCPUType(handler: MessageHandler, executor: ExecutorService): Pipeline = {
    return registry(handler, executor)
  }

  def regForIOType(handler: MessageHandler): Pipeline = {
    return registry(handler, 30)
  }

  def regForIOType(handler: MessageHandler, threadCount: Int): Pipeline = {
    return registry(handler, threadCount)
  }

  def regForIOType(handler: MessageHandler, executor: ExecutorService): Pipeline = {
    return registry(handler, executor)
  }


  def regGroup(group: Set[HandlerConfig]): Pipeline = {
    configs.addAll(group)
    this
  }

  def start(): Unit = { //fill queue
    starter.setQueue(msgQueueProxy)
    val mesTypeMap = configs.to(LazyList)
      .map(c => {
        c.handler.setQueue(msgQueueProxy)
        c
      })
      .groupBy(_.handler.getClass.hashCode())
    //      .collect(Collectors.groupingBy(HandlerConfig.mesCode))
    //start message driven
    listenerExecutorService.execute(() => {
      var running = true
      while ( {
        running
      }) try {
        val mes = msgQueueProxy.poll(5, TimeUnit.MINUTES)
        if (mes != null) {
          mesTypeMap.get(mes.code) match {
            case Some(configs) =>
              for (c <- configs) {
                c.executor.execute(() => {
                  try c.handler.process(mes)
                  catch {
                    case e: Exception =>
                      e.printStackTrace()
                  }

                })
              }
            case _ =>
          }
        }
        else {
          logger.info("listener task time out!!!")
          running = false
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
    })


    starter.process()
  }
}
