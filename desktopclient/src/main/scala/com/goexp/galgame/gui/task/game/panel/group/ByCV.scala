package com.goexp.galgame.gui.task.game.panel.group

import java.util

import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.panel.group.node.{DataItem, SampleItem}
import com.typesafe.scalalogging.Logger
import javafx.concurrent.Task

import scala.jdk.CollectionConverters._

class ByCV(val groupGames: util.List[Game]) extends Task[util.List[DataItem]] {
  private[task] val logger = Logger(getClass)

  override protected def call: util.List[DataItem] = createTagGroup(groupGames)

  private def createTagGroup(filteredGames: util.List[Game]) = {
    filteredGames.asScala.to(LazyList)
      .filter(g => Option(g.gameCharacters).map(_.size()).getOrElse(0) > 0)
      .flatMap { g =>
        g.gameCharacters.asScala.to(LazyList)
          .map { p => p.getShowCV() }
          .filter {
            _.isDefined
          }
          .map { c => c.get }
        //          .filter(t => Strings.isNotEmpty(t))
      }

      .groupBy(s => s).to(LazyList)
      .sortBy { case (_, v) => v.size }.reverse
      //        .take(20)
      .map { case (key, value) =>
        logger.trace(s"<createTagGroup> Name:$key,Value:${value.size}")
        SampleItem(key, value.size).asInstanceOf[DataItem]

      }.asJava
  }

}
