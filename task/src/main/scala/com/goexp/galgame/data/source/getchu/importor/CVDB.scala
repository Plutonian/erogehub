package com.goexp.galgame.data.source.getchu.importor

import java.time.LocalDate

import com.goexp.common.db.mongo.DBOperatorTemplate
import com.goexp.galgame.data.source.getchu.DB_NAME
import com.goexp.galgame.common.model.CV
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates.{combine, set}

object CVDB {
  val tlp = new DBOperatorTemplate(DB_NAME, "cv")

  def updateStatistics(item: CV, start: LocalDate, end: LocalDate, size: Int) =
    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(item.id), combine(
        set("start", start),
        set("end", end),
        set("size", size)
      ))
    })

}
