package com.goexp.galgame.gui.task.game.panel.group

import java.util

import com.goexp.common.util.string.Strings
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.panel.group.node.{DataItem, SampleItem}
import com.typesafe.scalalogging.Logger
import javafx.concurrent.Task

import scala.jdk.CollectionConverters._

class ByTag(val groupGames: util.List[Game]) extends Task[util.List[DataItem]] {
  private[task] val logger = Logger(getClass)

  override protected def call: util.List[DataItem] = createTagGroup(groupGames)

  private def createTagGroup(filteredGames: util.List[Game]) = {
    filteredGames.asScala.to(LazyList)
      .filter(g => g.tag.size > 0)
      .flatMap(g => g.tag.asScala.to(LazyList).filter(t => Strings.isNotEmpty(t)))

      //Stream{String,String,String,String...}
      .groupBy(s => s).to(LazyList)
      .sortBy { case (_, v) => v.size }.reverse
      //        .take(20)
      .map { case (key, value) =>
        logger.trace(s"<createTagGroup> Name:$key,Value:${value.size}")
        SampleItem(key, value.size).asInstanceOf[DataItem]
      }.asJava
  }

}
