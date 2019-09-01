package com.goexp.galgame.data.piplline.core

import java.util.concurrent.{ArrayBlockingQueue, TimeUnit}

import com.goexp.galgame.data.piplline.exception.RuntimeInterruptedException

class MessageQueueProxy[T <: Message](val capacity: Int) {
  private val msgQueue = new ArrayBlockingQueue[T](capacity)

  def offer(o: T): Boolean = try msgQueue.offer(o, 60, TimeUnit.MINUTES)
  catch {
    case e: InterruptedException =>
      e.printStackTrace()
      throw new RuntimeInterruptedException(e)
  }

  def offer(o: T, timeout: Long, unit: TimeUnit): Boolean = try msgQueue.offer(o, timeout, unit)
  catch {
    case e: InterruptedException =>
      e.printStackTrace()
      throw new RuntimeInterruptedException(e)
  }

  def pull: T = try msgQueue.poll(60, TimeUnit.MINUTES)
  catch {
    case e: InterruptedException =>
      e.printStackTrace()
      throw new RuntimeInterruptedException(e)
  }

  def poll(timeout: Long, unit: TimeUnit): T = try msgQueue.poll(timeout, unit)
  catch {
    case e: InterruptedException =>
      e.printStackTrace()
      throw new RuntimeInterruptedException(e)
  }
}