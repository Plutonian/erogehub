package com.goexp.galgame.data.task

import java.util.Objects

import com.goexp.galgame.data.task.local.{GetTrueCVTask, GroupBrandTask, MarkSameGameTask}
import com.goexp.galgame.data.task.others.UpdateBrandTask
import com.goexp.galgame.data.task.others.guide.Seiya_saiga_com

object ImporterStart {

  private val funcTable =
    List(
      ("upgrade-game-date", "Upgrade game info from date to another", FromDateRangeTask.main _),
      //      ("upgrade-game-brand", "Upgrade game info brand by brand", FromAliveBrandTask.main _),
      ("upgrade-brand", "Upgrade brand info ", UpdateBrandTask.main _),
      ("upgrade-guide", "Upgrade game guide", Seiya_saiga_com.main _),
      ("get-truecv", "Get real CV name", GetTrueCVTask.main _),
      ("mark-same", "Mark the same game", MarkSameGameTask.main _),
      ("group-brand", "Group brand", GroupBrandTask.main _)
      //      ("import-tag", "Import tag info once", ImportOnceTagTask.main _),
      //      ("import-game-local", "Import from local cache once", ImportFromLocalAliveBrandTask.main _)
    )

  def main(args: Array[String]) = {
    if (args.isEmpty) showHowToUse() else exec(args)
  }

  def exec(args: Array[String]) = {

    Objects.requireNonNull(args)

    funcTable.toStream
      .find({ case (name, _, _) => args(0) == name })
    match {
      case Some(peer) =>
        val (_, _, func) = peer
        func(args)
      case None => this.showHowToUse()
    }
  }

  def showHowToUse() = funcTable.foreach({ case (name, desc, _) => print(s"$name:$desc\n") })

}