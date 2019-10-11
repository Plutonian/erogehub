package com.goexp.galgame.data.task

import java.util.Objects

import com.goexp.galgame.data.task.local.cal.{CalBrandGameTask, CalCVGameTask}
import com.goexp.galgame.data.task.local.getimage.{ByBrand, ByDateRange}
import com.goexp.galgame.data.task.local.{CleanSameGameTask, GetTrueCVTask, GroupBrandTask, MarkSameGameTask}
import com.goexp.galgame.data.task.others.UpdateBrandTask
import com.goexp.galgame.data.task.others.guide.Seiya_saiga_com

object ImporterStart {

  private val funcTable =
    List(
      ("upgrade-game", "Upgrade game info from one date to another", FromDateRangeTask.main _),
      ("upgrade-brand", "Upgrade brand info ", UpdateBrandTask.main _),
      ("upgrade-guide", "Upgrade game guide", Seiya_saiga_com.main _),
      ("get-truecv", "Get real CV name", GetTrueCVTask.main _),
      ("cal-brand-statistics", "Get statistics of brand", CalBrandGameTask.main _),
      ("cal-cv-statistics", "Get statistics of cv", CalCVGameTask.main _),
      ("mark-same", "Mark the same game", MarkSameGameTask.main _),
      ("group-brand", "Group brand", GroupBrandTask.main _),
      ("clean", "Clean same game img cache", CleanSameGameTask.main _),
      ("get-image-range", "Download game img by date range", ByDateRange.main _),
      ("get-image-brand", "Download game img by brand", ByBrand.main _)
    )

  def main(args: Array[String]) = {
    if (args.isEmpty) showHowToUse() else exec(args)
  }

  def exec(args: Array[String]) = {

    Objects.requireNonNull(args)

    funcTable.to(LazyList)
      .find { case (name, _, _) => args(0) == name } match {
      case Some(peer) =>
        val (_, _, func) = peer
        func(args.drop(1))
      case None => this.showHowToUse()
    }
  }

  def showHowToUse() = funcTable.foreach({ case (name, desc, _) => print(s"$name:$desc\n") })

}