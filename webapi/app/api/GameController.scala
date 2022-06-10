package api

import api.common.ExpendResult.ToJson
import com.goexp.common.util.string.Strings
import com.goexp.db.mongo.DBOperator
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.Config.DB_NAME
import com.goexp.galgame.common.model.Emotion
import com.goexp.galgame.common.model.game.GameCharacter
import com.goexp.galgame.data.model.Brand
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates.set
import entity.group._
import org.bson.BsonDocument
import play.libs.Json
import play.mvc.Controller
import play.mvc.Http.Request
import play.mvc.Results.{notFound, ok}
import qurey.GameFullQuery

import java.time.LocalDate
import scala.jdk.CollectionConverters._


class GameController extends Controller {

  implicit class Pre(where: BsonDocument) {

    def preProcess() = {
      //      Filters.and(where, Filters.gt("state", GameState.BLOCK.value))
      where
    }
  }


  api.Config.init()


  val tlp = new DBOperator(Config.DB_STRING, DB_NAME, "game")

  def delete(id: Int) = {
    println(id)

    tlp.exec(documentMongoCollection => {
      documentMongoCollection.deleteOne(Filters.eq(id))
    })

    ok(Json.toJson("OK")).asJson()
  }

  def info(id: Int) = {
    GameFullQuery().where(Filters.eq(id)).one() match {
      case Some(g) =>
        ok(Json.toJson(g)).asJson()
      case None => notFound()
    }
  }

  def query(request: Request) = {
    val where = request.queryString("filter").orElseThrow()

    println(BsonDocument.parse(where).preProcess())

    val list = GameFullQuery().where(BsonDocument.parse(where).preProcess()).list()

    ok(Json.toJson(Option(list).getOrElse(List().asJava))).asJson()
  }

  def groupByEmotion(request: Request) = {
    val where = request.queryString("filter").orElseThrow()

    val list = GameFullQuery().where(BsonDocument.parse(where).preProcess()).scalaList()

    val cvlist = list.to(LazyList)

      .groupBy(s => s.emotion).to(LazyList)
      .sortBy { case (_, count) => count.size }.reverse
      //        .take(20)
      .map { case (emotion, games) =>
        EmotionItem(emotion.toString, games.size, emotion, games.toArray)
      }.asJava


    ok(Json.toJson(cvlist)).asJson()
  }

  def groupByCV(request: Request) = {
    val where = request.queryString("filter").orElseThrow()

    val list = GameFullQuery().where(BsonDocument.parse(where).preProcess()).scalaList()

    val cvlist = list.to(LazyList)

      //skip all empty gameCharacters
      .filter(g => Option(g.gameCharacters).map(_.size()).getOrElse(0) > 0)
      .flatMap { g =>
        g.gameCharacters.asScala.to(LazyList)
          .map { case GameCharacter(_, cv, _, trueCV, _, _) =>

            if (Strings.isNotEmpty(trueCV))
              (s"$trueCV", true)
            else
              (s"$cv", false)
          }
      }
      //skip all none cv or empty cv
      .filter { case ((name, _)) => Strings.isNotEmpty(name) }

      .groupBy(s => s).to(LazyList)
      .sortBy { case (_, count) => count.size }.reverse
      //        .take(20)
      .map { case ((cv, isReal), games) =>
        SampleCVItem(cv, games.size, isReal)
      }.asJava


    ok(Json.toJson(cvlist)).asJson()
  }

  def groupByTag(request: Request) = {
    val where = request.queryString("filter").orElseThrow()

    val list = GameFullQuery().where(BsonDocument.parse(where).preProcess()).scalaList()

    val taglist = list.to(LazyList)
      //skip all empty tags
      .filter(g => Option(g.tag).map(_.size()).getOrElse(0) > 0)
      .flatMap(g => g.tag.asScala.to(LazyList).filter(t => Strings.isNotEmpty(t)))

      .groupBy(s => s).to(LazyList)
      .sortBy { case (_, v) => v.size }.reverse
      //        .take(20)
      .map { case (tag, games) =>
        SampleItem(tag, games.size)
      }.asJava


    ok(Json.toJson(taglist)).asJson()
  }

  def groupByDate(request: Request) = {
    val where = request.queryString("filter").orElseThrow()

    val list = GameFullQuery().where(BsonDocument.parse(where).preProcess()).scalaList()

    val years = list.to(LazyList)
      .filter(game => game.publishDate != null)
      .groupBy(game => Option(game.publishDate).map(_.getYear).getOrElse(0)).to(LazyList)
      .filter { case (year, _) => year != 0 }
      .sortBy { case (year, _) => year }.reverse
      .map { case (year, games) =>

        val monthNode = games.groupBy(game => Option(game.publishDate).map(_.getMonthValue).getOrElse(0)).to(LazyList)
          .sortBy { case (month, _) => month }.reverse
          .map { case (month, games) =>
            new DateItem(
              s"${month}月",
              LocalDate.of(year, month, 1),
              LocalDate.of(year, month, 1).plusMonths(1).minusDays(1),
              month,
              games.size,
              DateType.MONTH,
              null
            )

          }.toArray[DateItem]


        new DateItem(
          s"${year}年",
          LocalDate.of(year, 1, 1),
          LocalDate.of(year, 12, 31),
          year,
          games.size,
          DateType.YEAR,
          monthNode
        )
      }.asJava

    ok(Json.toJson(years)).asJson()
  }

  def groupByBrand(request: Request) = {
    val where = request.queryString("filter").orElseThrow()

    val list = GameFullQuery().where(BsonDocument.parse(where).preProcess()).scalaList()

    val nodes = list.to(LazyList)
      // skip null brand
      .filter(_.brand != null)
      .groupBy { game =>
        val brand = game.brand

        Option(brand.comp).getOrElse(brand)
      }
      .to(LazyList)

      .map {
        case (comp: String, v) =>

          val brandNodes = v.groupBy(_.brand).to(LazyList)
            .sortBy { case (brand, _) => brand.state.value }.reverse
            .map { case (brand, games) => BrandItem(brand.name, games.size, null, brand, games.toArray, null) }
            .toArray

          if (brandNodes.size == 1)
            brandNodes(0)
          else
            BrandItem(comp, v.size, comp, null, null, brandNodes)

        case (brand: Brand, v) =>
          BrandItem(brand.name, v.size, null, brand, v.toArray, null)
      }
      .sortBy { item => item.count }.reverse
      .asJava

    ok(Json.toJson(nodes)).asJson()
  }

  def groupByStar(request: Request) = {
    val where = request.queryString("filter").orElseThrow()

    val list = GameFullQuery().where(BsonDocument.parse(where).preProcess()).scalaList()

    val nodes = list.to(LazyList)
      .groupBy {
        _.star
      }
      .to(LazyList)
      .sortBy { case (star, _) => star }
      .map {
        case (star: Int, v) => StarItem(star.toString, v.size, star, v.toArray)
      }
      .asJava

    ok(Json.toJson(nodes)).asJson()
  }

  def markSame(id: Int, isSame: Boolean) = {

    println(id, isSame)

    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(id), set("isSame", isSame))
    })

    ok(Json.toJson("OK")).asJson()

  }

  def changeEmotion(id: Int, emotion: Int) = {

    println(id, emotion)

    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(id), set("emotion", emotion))
    })

    ok(Json.toJson("OK")).asJson()

  }

  def changePlayState(id: Int, state: Int) = {

    println(id, state)

    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(id), set("playState", state))
    })

    ok(Json.toJson("OK")).asJson()

  }

  def changeStar(id: Int, star: Int) = {

    println(id, star)

    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(id), set("star", star))
    })

    ok(Json.toJson("OK")).asJson()

  }

  def changeLocation(id: Int, location: Int) = {

    println(id, location)

    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(id), set("location", location))
    })

    ok(Json.toJson("OK")).asJson()

  }

  def block(brandId: Int) = {
    changeEmotionByBrand(brandId, Emotion.HATE.value)
  }

  def normal(brandId: Int) = {
    changeEmotionByBrand(brandId, Emotion.NORMAL.value)
  }

  private def changeEmotionByBrand(brandId: Int, state: Int) = {
    println(brandId, state)

    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateMany(Filters.eq("brandId", brandId), set("emotion", state))
    })

    ok(Json.toJson("OK")).asJson()
  }


}

