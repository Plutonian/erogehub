package qurey

import com.goexp.db.mongo.{DBQuery, ObjectCreator}
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.Config.DB_NAME
import com.goexp.galgame.common.db.mongo.query.CommonBrandCreator
import com.goexp.galgame.common.model.Emotion
import com.mongodb.client.model.Filters
import com.typesafe.scalalogging.Logger
import entity.{Brand, Series}
import org.bson.Document

import java.util
import scala.jdk.CollectionConverters._

object BrandQuery {
  private val tlp = DBQuery[Brand](Config.DB_STRING, DB_NAME, "brand", Creator).build

  def apply() = tlp


  private object SeriesCreator extends ObjectCreator[Series] {
    private val logger = Logger(SeriesCreator.getClass)

    override def create(doc: Document): Series = {
      logger.trace(s"<create> doc=${doc}")

      Series(
        name = doc.get("name", classOf[String]),
        games = {
          Option(doc.get("games", classOf[util.List[Int]])).map { ids =>
            GameFullQuery().where(Filters.in("_id", ids)).list()
          }.orNull
        }
      )

      //      b.tag = Option(doc.get("tag", classOf[util.List[String]])).map {
      //        _.asScala.toList
      //      }.orNull
      //      b.state = Emotion.from(doc.getInteger("type"))
      //
      //      b
    }
  }

  private object Creator extends ObjectCreator[Brand] {
    private val logger = Logger(Creator.getClass)

    override def create(doc: Document): Brand = {
      logger.trace(s"<create> doc=${doc}")

      val parentCreator = new CommonBrandCreator(new Brand)
      val b = parentCreator.create(doc).asInstanceOf[Brand]
      b.tag = Option(doc.get("tag", classOf[util.List[String]])).map {
        _.asScala.toList
      }.orNull
      b.emotion = Emotion.from(doc.getInteger("type"))

      b.series =
        Option(doc.get("series").asInstanceOf[util.List[Document]])
          .map {
            _.asScala.to(LazyList).map(SeriesCreator.create).asJava
          }
          .getOrElse(util.List.of[Series]())

      b
    }
  }

}