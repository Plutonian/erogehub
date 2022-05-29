package api

import com.goexp.db.mongo.DBOperator
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.Config.DB_NAME
import com.goexp.galgame.data.source.getchu.query.BrandQuery
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates.set
import org.bson.BsonDocument
import play.libs.Json
import play.mvc.Http.Request
import play.mvc.Results.ok

import scala.jdk.CollectionConverters._


class BrandController {

  val tlp = new DBOperator(Config.DB_STRING, DB_NAME, "brand")

  api.Config.init()

  def info(id: Int) = {

    val maybeBrand = BrandQuery()
      .where(Filters.eq(id)).one().orNull

    ok(Json.toJson(maybeBrand)).as("application/json; charset=utf-8")

  }

  def query(request: Request) = {
    val where = request.queryString("filter").orElseThrow()

    println(where)

    val list = BrandQuery().where(BsonDocument.parse(where)).list()

    ok(Json.toJson(Option(list).getOrElse(List().asJava))).as("application/json; charset=utf-8")
  }

  def changeState(id: Int, state: Int) = {

    println(id, state)

    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(id), set("type", state))
    })

    ok(Json.toJson("OK")).as("application/json; charset=utf-8")

  }

  //  def byComp(comp: String) = {
  //    BrandQuery().where(Filters.eq("comp", comp))
  //      .sort(and(descending("type"), descending("name")))
  //      .scalaList()
  //  }
}