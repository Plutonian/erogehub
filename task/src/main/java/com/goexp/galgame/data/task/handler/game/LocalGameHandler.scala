package com.goexp.galgame.data.task.handler.game

import java.io.IOException
import java.nio.file.Files
import java.util.Objects

import com.goexp.galgame.data.Config
import com.goexp.galgame.data.piplline.core.{Message, MessageQueueProxy}
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler
import com.goexp.galgame.data.task.handler.MesType
import org.slf4j.LoggerFactory

class LocalGameHandler extends DefaultMessageHandler[Int] {
  final private val logger = LoggerFactory.getLogger(classOf[LocalGameHandler])

  override def process(message: Message[Int], msgQueue: MessageQueueProxy[Message[_]]) = {
    val gid = message.entity
    logger.debug("<Game> {}", gid)

    val bytes = (gid, Objects.requireNonNull(getContent(gid), gid.toString))
    msgQueue.offer(new Message[(Int, Array[Byte])](MesType.ContentBytes, bytes))
  }

  private def getContent(id: Int) =
    try {
      val path = Config.GAME_CACHE_ROOT.resolve(s"$id.bytes")
      Files.readAllBytes(path)
    } catch {
      case e: IOException =>
        e.printStackTrace()
        throw new RuntimeException(e)
    }
}