package api

import api.common.ExpendResult.ToJson
import com.goexp.common.util.string.Strings
import com.goexp.db.mongo.DBOperator
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.Config.DB_NAME
import com.goexp.galgame.common.db.mongo.query.GameQuery
import com.goexp.galgame.common.model.Emotion
import com.goexp.galgame.common.model.game.GameCharacter
import com.goexp.galgame.data.model.Brand
import com.mongodb.client.model.{Accumulators, Aggregates, Filters, Projections, Sorts}
import com.mongodb.client.model.Updates.{combine, set}
import entity.group.*
import org.bson.BsonDocument
import play.libs.Json
import play.mvc.Controller
import play.mvc.Http.Request
import play.mvc.Results.{notFound, ok}
import qurey.GameFullWebQuery
import qurey.GameWebQuery.SimpleGameCreator

import java.time.LocalDate
import scala.jdk.CollectionConverters.*


class GameController extends Controller {

  val pageItemSize = 40

  implicit class Pre(where: BsonDocument) {

    def preProcess() = {
      //      Filters.and(where, Filters.gt("state", GameState.BLOCK.value))
      where
    }
  }


  api.Config.init()


  val tpl = new DBOperator(Config.DB_STRING, DB_NAME, "game")

  def delete(id: Int) = {
    println(id)

    tpl.exec(documentMongoCollection => {
      documentMongoCollection.deleteOne(Filters.eq(id))
    })

    ok(Json.toJson("OK")).asJson()
  }

  def info(id: Int) = {
    GameFullWebQuery().where(Filters.eq(id)).one() match {
      case Some(g) =>
        ok(Json.toJson(g)).asJson()
      case None => notFound()
    }
  }

  def query(request: Request) = {
    val where = request.queryString("filter").orElseThrow()

    println(BsonDocument.parse(where).preProcess())

    val list = GameFullWebQuery().where(BsonDocument.parse(where).preProcess()).list()

    ok(Json.toJson(Option(list).getOrElse(List().asJava))).asJson()
  }

  def count(request: Request) = {
    val body = request.body.asJson
    val where = body.get("filter").asText()

    val count = GameQuery().countDocuments(BsonDocument.parse(where))

    ok(Json.toJson(count)).asJson()
  }

  def queryData(request: Request) = {
    val body = request.body.asJson
    val where = body.get("filter").asText()
    val page = Option(body.get("page")).map(_.asInt()).getOrElse(1)
    val pageSize = Option(body.get("pageItemSize")).map(_.asInt()).getOrElse(pageItemSize)


    val findIterable =
      if (where != null && where != "undefined") {
        GameQuery()
          .find()
          .filter(BsonDocument.parse(where))
          .sort(Sorts.orderBy(Sorts.descending("publishDate")))
      }
      else {
        GameQuery()
          .find()
          .sort(Sorts.orderBy(Sorts.descending("publishDate")))
      }

    val list = findIterable
      .skip((page - 1) * pageSize)
      .limit(pageSize)
      .asScala.to(LazyList)
      .map(doc => {
        doc.append("id", doc.getInteger("_id"))
        doc
      })
//      .map(SimpleGameCreator.create)
      .toList
//      .asJava

//    ok(Json.toJson(Option(list).getOrElse(List().asJava))).asJson()

    ok(Json.toJson(list.asJava)).asJson()
  }

  def groupByEmotion(request: Request) = {
    val where = request.queryString("filter").orElseThrow()

    val pipeline = List(
      Aggregates.`match`(BsonDocument.parse(where)),
      Aggregates.group("$emotion", Accumulators.sum("count", 1)),
      Aggregates.sort(Sorts.descending("_id")),
      Aggregates.project(Projections.fields(
        Projections.computed("emotion", "$_id"),
        Projections.include("count"),
        Projections.excludeId()
      ))
    ).asJava

    val results = GameQuery()
      .aggregate(pipeline)
      .asScala
      .to(LazyList)
      .asJava

    ok(Json.toJson(results)).asJson()
  }

  def groupByLocation(request: Request) = {
    val where = request.queryString("filter").orElseThrow()

    val pipeline = List(
      Aggregates.`match`(BsonDocument.parse(where)),
      Aggregates.group("$location", Accumulators.sum("count", 1)),
      Aggregates.sort(Sorts.descending("_id")),
      Aggregates.project(Projections.fields(
        Projections.computed("location", "$_id"),
        Projections.include("count"),
        Projections.excludeId()
      ))
    ).asJava

    val results = GameQuery()
      .aggregate(pipeline)
      .asScala
      .to(LazyList)
      .asJava

    ok(Json.toJson(results)).asJson()
  }

  def groupByPlayState(request: Request) = {
    val where = request.queryString("filter").orElseThrow()

    val pipeline = List(
      Aggregates.`match`(BsonDocument.parse(where)),
      Aggregates.group("$playState", Accumulators.sum("count", 1)),
      Aggregates.sort(Sorts.descending("_id")),
      Aggregates.project(Projections.fields(
        Projections.computed("playState", "$_id"),
        Projections.include("count"),
        Projections.excludeId()
      ))
    ).asJava

    val results = GameQuery()
      .aggregate(pipeline)
      .asScala
      .to(LazyList)
      .asJava

    ok(Json.toJson(results)).asJson()
  }

  def groupByCV(request: Request) = {
    val where = request.queryString("filter").orElseThrow()

    val list = GameFullWebQuery().where(BsonDocument.parse(where).preProcess()).scalaList()

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

  def groupByTrueCV(request: Request) = {
    val where = request.queryString("filter").orElseThrow()

    val pipeline = List(
      Aggregates.`match`(BsonDocument.parse(where)),
      Aggregates.unwind("$gamechar"),
      Aggregates.`match`(Filters.and(
        Filters.ne("gamechar.truecv", null),
        Filters.ne("gamechar.truecv", "")
      )),
      Aggregates.group("$gamechar.truecv", Accumulators.sum("count", 1)),
      Aggregates.sort(Sorts.descending("count")),
      Aggregates.project(Projections.fields(
        Projections.computed("title", "$_id"),
        Projections.include("count"),
        Projections.excludeId()
      ))
    ).asJava

    val results = GameQuery()
      .aggregate(pipeline)
      .asScala
      .to(LazyList)
      .map(doc => SampleCVItem(doc.getString("title"), doc.getInteger("count"), true))
      .asJava

    ok(Json.toJson(results)).asJson()
  }

  def groupByTag(request: Request) = {
    val where = request.queryString("filter").orElseThrow()

    val pipeline = List(
      Aggregates.`match`(BsonDocument.parse(where)),
      Aggregates.unwind("$tag"),
      Aggregates.`match`(Filters.ne("tag", "")),
      Aggregates.group("$tag", Accumulators.sum("count", 1)),
      Aggregates.sort(Sorts.descending("count")),
      Aggregates.project(Projections.fields(
        Projections.computed("title", "$_id"),
        Projections.include("count"),
        Projections.excludeId()
      ))
    ).asJava

    val results = GameQuery()
      .aggregate(pipeline)
      .asScala
      .to(LazyList)
      .asJava

    ok(Json.toJson(results)).asJson()
  }

  def groupByDate(request: Request) = {
    val where = request.queryString("filter").orElseThrow()

    val list = GameFullWebQuery().where(BsonDocument.parse(where).preProcess()).scalaList()

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

    val list = GameFullWebQuery().where(BsonDocument.parse(where).preProcess()).scalaList()

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

          if (brandNodes.length == 1)
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

    val pipeline = List(
      Aggregates.`match`(BsonDocument.parse(where)),
      Aggregates.group("$star", Accumulators.sum("count", 1)),
      Aggregates.sort(Sorts.ascending("_id")),
      Aggregates.project(Projections.fields(
        Projections.computed("star", "$_id"),
        Projections.include("count"),
        Projections.excludeId()
      ))
    ).asJava

    val results = GameQuery()
      .aggregate(pipeline)
      .asScala
      .to(LazyList)
      .asJava

    ok(Json.toJson(results)).asJson()
  }

  def markSame(id: Int, isSame: Boolean) = {

    tpl.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(id), set("isSame", isSame))
    })

    ok(Json.toJson("OK")).asJson()

  }

  def setTitle(id: Int, request: Request) = {

    val node = request.body().asJson()


    val mainTitle = node.get("mainTitle").textValue()
    val subTitle = node.get("subTitle").textValue()


    tpl.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(id), combine(
        set("mainTitle", Option(mainTitle).map(_.trim).orNull),
        set("subTitle", Option(subTitle).map(_.trim).orNull)
      ))
    })

    ok(Json.toJson("OK")).asJson()

  }

  def changeEmotion(id: Int, emotion: Int) = {


    tpl.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(id), set("emotion", emotion))
    })

    ok(Json.toJson("OK")).asJson()

  }

  def changePlayState(id: Int, state: Int) = {

    tpl.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(id), set("playState", state))
    })

    ok(Json.toJson("OK")).asJson()

  }

  def changeStar(id: Int, star: Int) = {

    tpl.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(id), set("star", star))
    })

    ok(Json.toJson("OK")).asJson()

  }

  def changeLocation(id: Int, location: Int) = {


    tpl.exec(documentMongoCollection => {
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

  def setCharMan(id: Int, index: Int) = {

    tpl.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.and(Filters.eq(id), Filters.eq("gamechar.index", index)), set("gamechar.$.man", true))
    })

    ok(Json.toJson("OK")).asJson()
  }

  def setCharCV(id: Int, index: Int, request: Request) = {
    val node = request.body().asJson()
    val cv = node.get("cv").textValue()

    tpl.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.and(Filters.eq(id), Filters.eq("gamechar.index", index)), set("gamechar.$.cv", cv))
    })

    ok(Json.toJson("OK")).asJson()

  }

  def updateTags(id: Int, request: Request) = {
    val body = request.body.asJson
    val tags = body.get("tags").elements().asScala.to(LazyList).map(_.asText()).asJava


    tpl.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(id), combine(
        set("tag", tags),
      ))
    })

    ok(Json.toJson("OK")).asJson()
  }

  //  def clearCharCV(id: Int, index: Int) = {
  //
  //    tpl.exec(documentMongoCollection => {
  //      documentMongoCollection.updateOne(Filters.and(Filters.eq(id), Filters.eq("gamechar.index", index)), combine(
  //        unset("gamechar.$.cv"),
  //        unset("gamechar.$.truecv"),
  //      ))
  //    })
  //
  //    ok(Json.toJson("OK")).asJson()
  //
  //  }

  private def changeEmotionByBrand(brandId: Int, state: Int) = {

    tpl.exec(documentMongoCollection => {
      documentMongoCollection.updateMany(Filters.eq("brandId", brandId), set("emotion", state))
    })

    ok(Json.toJson("OK")).asJson()
  }


}

