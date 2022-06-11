package qurey

import com.goexp.common.util.date.DateUtil
import com.goexp.db.mongo.ObjectCreator
import com.goexp.galgame.common.model.game.{EmotionStatistics, GameStatistics, LocationStatistics, StarStatistics}
import com.typesafe.scalalogging.Logger
import org.bson.Document

object StatCreators {
  private val logger = Logger(StatCreators.getClass)


  val statisticsCreator: ObjectCreator[GameStatistics] = (doc: Document) => {
    logger.trace(s"<Doc> $doc")

    val statistics = GameStatistics(
      Option(doc.getDate("start")).map(DateUtil.toLocalDate).orNull,
      Option(doc.getDate("end")).map(DateUtil.toLocalDate).orNull,
      doc.getInteger("count", 0),
      doc.getInteger("realCount", 0),
      Option(doc.get("emotion").asInstanceOf[Document]).map(emotionCreator.create).orNull,
      Option(doc.get("star").asInstanceOf[Document]).map(starCreator.create).orNull,
      Option(doc.get("location").asInstanceOf[Document]).map(locCreator.create).orNull
    )

    logger.trace(s"<statistics> ${statistics}")

    statistics

  }
  private val emotionCreator: ObjectCreator[EmotionStatistics] = (doc: Document) => {
    logger.trace(s"<Doc> $doc")

    val statistics = EmotionStatistics(
      doc.getInteger("LIKE"),
      doc.getInteger("HOPE"),
      doc.getInteger("NORMAL"),
      doc.getInteger("HATE")
    )

    logger.trace(s"<statistics> ${statistics}")

    statistics

  }
  private val starCreator: ObjectCreator[StarStatistics] = (doc: Document) => {
    logger.trace(s"<Doc> $doc")

    val statistics = StarStatistics(
      doc.getInteger("zero"),
      doc.getInteger("one"),
      doc.getInteger("two"),
      doc.getInteger("three"),
      doc.getInteger("four"),
      doc.getInteger("five")
    )

    logger.trace(s"<statistics> ${statistics}")

    statistics

  }
  private val locCreator: ObjectCreator[LocationStatistics] = (doc: Document) => {
    logger.trace(s"<Doc> $doc")

    val statistics = LocationStatistics(
      doc.getInteger("LOCAL"),
      doc.getInteger("REMOTE")
    )

    logger.trace(s"<statistics> ${statistics}")

    statistics

  }
}
