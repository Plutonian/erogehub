package com.goexp.galgame.data.piplline.core

import java.util.concurrent.{ExecutorService, Executors, TimeUnit}

import com.goexp.galgame.data.piplline.exception.RuntimeInterruptedException
import com.goexp.galgame.data.piplline.handler.HandlerConfig
import org.slf4j.LoggerFactory

import scala.collection.mutable

class Piplline(private[this] val starter: Starter) {


  private val logger = LoggerFactory.getLogger(classOf[Piplline])

  private var msgQueueProxy = new MessageQueueProxy[Message](1000)


  private val listenerExecutorService = Executors.newSingleThreadExecutor

  private val configs = mutable.Set[HandlerConfig]()


  def registry(config: HandlerConfig): Piplline = {
    configs.add(config)
    return this
  }


  private def registry(mesType: Int, handler: MessageHandler, executor: ExecutorService): Piplline = {
    configs.add(HandlerConfig(mesType, handler, executor))
    return this
  }

  private def registry(mesType: Int, handler: MessageHandler, threadCount: Int): Piplline = {
    configs.add(new HandlerConfig(mesType, handler, Executors.newFixedThreadPool(threadCount)))
    return this
  }


  def regForCPUType(mesCode: Int, handler: MessageHandler): Piplline = {
    return registry(mesCode, handler, Runtime.getRuntime.availableProcessors)
  }

  def regForCPUType(mesCode: Int, handler: MessageHandler, threadCount: Int): Piplline = {
    return registry(mesCode, handler, threadCount)
  }

  def regForCPUType(mesCode: Int, handler: MessageHandler, executor: ExecutorService): Piplline = {
    return registry(mesCode, handler, executor)
  }

  def regForIOType(mesCode: Int, handler: MessageHandler): Piplline = {
    return registry(mesCode, handler, 30)
  }

  def regForIOType(mesCode: Int, handler: MessageHandler, threadCount: Int): Piplline = {
    return registry(mesCode, handler, threadCount)
  }

  def regForIOType(mesCode: Int, handler: MessageHandler, executor: ExecutorService): Piplline = {
    return registry(mesCode, handler, executor)
  }


  def regGroup(group: Set[HandlerConfig]): Piplline = {
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
      .groupBy(_.mesCode)
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
