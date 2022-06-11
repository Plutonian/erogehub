package qurey

import com.goexp.db.mongo.{DBQuery, ObjectCreator}
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.Config.DB_NAME
import com.goexp.galgame.common.model.CV
import com.typesafe.scalalogging.Logger
import org.bson.Document

import java.util
import scala.jdk.CollectionConverters._

object CVQuery {
  private val tlp = DBQuery[CV](Config.DB_STRING, DB_NAME, "cv", CVCreator).build

  def apply() = tlp


  private object CVCreator extends ObjectCreator[CV] {

    private val logger = Logger(CVCreator.getClass)


    override def create(doc: Document): CV = {
      logger.trace(s"<Doc> $doc")

      val cv = new CV
      cv.id = doc.getInteger("_id")
      cv.name = doc.getString("name")
      cv.star = doc.getInteger("star")

      cv.tag = Option(doc.get("tag", classOf[util.List[String]])).map { l => l.asScala.toList }.orNull
      cv.nameStr = doc.getString("names")


      cv.statistics = Option(doc.get("statistics", classOf[Document])).map(StatCreators.statisticsCreator.create).orNull

      logger.trace(s"<CV> $cv")

      cv
    }
  }
}