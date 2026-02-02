package api

import api.common.ExpendResult.ToJson
import com.goexp.db.mongo.DBOperator
import com.goexp.galgame.common.Config
import com.goexp.galgame.common.Config.DB_NAME
import com.goexp.galgame.common.db.mongo.query.BrandQuery
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts.descending
import com.mongodb.client.model.Updates.set
import org.bson.BsonDocument
import play.libs.Json
import play.mvc.Http.Request
import play.mvc.Results.{notFound, ok}
import qurey.BrandWebQuery

import scala.jdk.CollectionConverters._


class BrandController {

  val tlp = new DBOperator(Config.DB_STRING, DB_NAME, "brand")

  api.Config.init()

  def info(id: Int) = {

    Option(BrandQuery()
      .find().filter(Filters.eq(id))
      .map(doc => {
        doc.append("id", doc.getInteger("_id"))
        doc
      })
      .first())

    match {
      case Some(maybeBrand) =>
        ok(Json.toJson(maybeBrand)).asJson()
      case None => notFound()
    }

    //    ok(Json.toJson(maybeBrand)).asJson()
  }

  def query(request: Request) = {
    val where = request.queryString("filter").orElseThrow()

    println(where)

    val list = BrandWebQuery().where(BsonDocument.parse(where)).sort(descending("type", "comp", "title")).list()

    ok(Json.toJson(Option(list).getOrElse(List().asJava))).asJson()
  }

  def changeState(id: Int, state: Int) = {

    tlp.exec(documentMongoCollection => {
      documentMongoCollection.updateOne(Filters.eq(id), set("type", state))
    })

    ok(Json.toJson("OK")).asJson()

  }

}
