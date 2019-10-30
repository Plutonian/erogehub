package com.goexp.galgame.data.source.erogamescape.query

import com.goexp.common.db.mongo.{DBQueryTemplate, ObjectCreator}
import com.goexp.common.util.date.DateUtil
import com.goexp.galgame.common.db.mongo.query.CommonGameCreator
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.source.erogamescape.DB_NAME
import com.goexp.galgame.data.source.erogamescape.parser.DetailPageParser.{BasicItem, OutLink}
import com.goexp.galgame.data.source.erogamescape.parser.ListPageParser.PageItem
import org.bson.Document
import org.slf4j.LoggerFactory

object GameQuery {

  val TABLE_NAME = "game"

  val simpleTlp = new DBQueryTemplate.Builder[Game](DB_NAME, TABLE_NAME, SimpleGame).build
  val gameItemTlp = new DBQueryTemplate.Builder[ParseLine](DB_NAME, TABLE_NAME, GameItemCreator)
    //    .defaultSelect(exclude("gamechar"))
    //    .defaultSelect(exclude("simpleImg"))
    .build


  object SimpleGame extends ObjectCreator[Game] {
    private val logger = LoggerFactory.getLogger(SimpleGame.getClass)

    override def create(doc: Document) = {

      val parentCreator = new CommonGameCreator(new Game)
      val g = parentCreator.create(doc).asInstanceOf[Game]

      g.brandId = Option(doc.getInteger("brandId")).map(_.toInt).getOrElse(0)
      //      g.state = Option(doc.getInteger("state")).map(s => GameState.from(s)).getOrElse(GameState.UNCHECKED)

      logger.debug("{}", g)
      g
    }
  }

  case class ParseLine(pageItem: PageItem, basicItem: BasicItem)

  object GameItemCreator extends ObjectCreator[ParseLine] {

    private val logger = LoggerFactory.getLogger(GameItemCreator.getClass)

    override def create(doc: Document) = {
      ParseLine(
        PageItem(
          id = doc.getInteger("_id"),
          name = doc.getString("name"),
          brandId = doc.getInteger("brandId"),
          middle = doc.getInteger("middle"),
          pian = doc.getInteger("pian"),
        ),
        BasicItem(
          OutLink(
            doc.getString("website"),
            doc.getInteger("getchuId"),
            doc.getString("dmmId")
          ),
          null,
          doc.getString("group"),
          Option(doc.getDate("publishDate")).map(DateUtil.toLocalDate).orNull
        )
      )
    }
  }

}