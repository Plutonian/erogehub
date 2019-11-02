package com.goexp.galgame.gui.task.game.panel.group

import java.util

import com.goexp.common.util.string.Strings
import com.goexp.galgame.gui.model.Game
import com.goexp.galgame.gui.task.game.panel.group.node.DefaultItem
import javafx.concurrent.Task
import com.typesafe.scalalogging.Logger

import scala.jdk.CollectionConverters._

class ByCV(val groupGames: util.List[Game]) extends Task[util.List[DefaultItem]] {
  private[task] val logger = Logger(getClass)

  override protected def call: util.List[DefaultItem] = createTagGroup(groupGames)

  private def createTagGroup(filteredGames: util.List[Game]) = {
    filteredGames.asScala.to(LazyList)
      .filter(g => Option(g.gameCharacters).map(_.size()).getOrElse(0) > 0)
      .flatMap(g =>
        g.gameCharacters.asScala.to(LazyList)
          .map(p => if (Strings.isNotEmpty(p.trueCV)) p.trueCV else if (Strings.isNotEmpty(p.cv)) p.cv else "")
          .filter(t => Strings.isNotEmpty(t))
      )

      .groupBy(s => s).to(LazyList)
      .sortBy({ case (_, v) => v.size }).reverse
      //        .take(20)
      .map({ case (key, value) =>
        logger.trace(s"<createTagGroup> Name:$key,Value:${value.size}")
        new DefaultItem(key, value.size)

      }).asJava
  }

}
