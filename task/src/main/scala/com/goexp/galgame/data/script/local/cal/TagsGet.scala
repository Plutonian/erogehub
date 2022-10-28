package com.goexp.galgame.data.script.local.cal

import com.atilika.kuromoji.unidic.{Token, Tokenizer}
import com.goexp.galgame.data.source.getchu.query.GameFullWithCharQuery
import com.mongodb.client.model.Filters
import com.typesafe.scalalogging.Logger

import scala.jdk.CollectionConverters.CollectionHasAsScala

object TagsGet {

//  val tokenizer = new Tokenizer

  private val builder = new Tokenizer.Builder()
//  builder.userDictionary(raw"/home/benbear/下载/unidic-csj-3.1.1/lex_3_1_change.csv")
  val tokenizer = builder.build()


  val groupSkipType = Set("空白", "感動詞", "記号", "助動詞", "助詞", "補助記号", "代名詞", "接続詞", "連体詞", "副詞", "動詞", "接尾辞", "接頭辞")
  val filterSkipType = Set("空白", "感動詞", "記号", "助動詞", "助詞")

  private val logger = Logger(TagsGet.getClass)

  def main(args: Array[String]): Unit = {

    args.length match {
      case 0 =>
        println("<gameid>")
      case 1 =>

        val id = args(0).toInt

        GameFullWithCharQuery().where(Filters.eq(id)).one() match {
          case Some(game) =>
            logger.info(s"Get Game: ${game.name}")


            println("Title: ")
            filterRet(tokenizer.tokenize(game.name))
            groupRet(tokenizer.tokenize(game.name))

            println("Intro: ")
            filterRet(tokenizer.tokenize(game.intro))
            groupRet(tokenizer.tokenize(game.intro))

            println("Story: ")
            filterRet(tokenizer.tokenize(game.story))
            groupRet(tokenizer.tokenize(game.story))


            if (Option(game.gameCharacters).map(_.size()).getOrElse(0) > 0) {

              println("Chars: ")

              game.gameCharacters.asScala
                .foreach {
                  gc =>
                    println(s"[${gc.index}]${gc.name}")
                    filterRet(tokenizer.tokenize(gc.intro))
                    groupRet(tokenizer.tokenize(gc.intro))
                }
            }


            val gcs = Option(game.gameCharacters).map(list => list.asScala.map(_.intro).mkString("\n")).getOrElse("")
            groupRet(tokenizer.tokenize(gcs))


            println("All: ")
            groupRet(tokenizer.tokenize(s"${game.name}\n${game.intro}\n${game.story}"))


          case None => println("no Game")
        }

    }


  }

  def groupRet(words: java.util.List[Token]): Unit = {

    words.asScala.to(LazyList)
      .filter { t => !groupSkipType.contains(t.getPartOfSpeechLevel1) }
      //      .filter { t => t.getPartOfSpeechLevel1 != "動詞" }
      .groupBy(t => t.getPartOfSpeechLevel1)
      .foreach { case (k, v) =>

        println(s"${k}=>")

        v.groupBy(t => t.getLemma).to(LazyList)
          .sortBy(item => item._2.size).reverse
          .foreach { case (k, v) =>
            println(s"$k (${v.size}) [${v.map(_.getPartOfSpeechLevel1).distinct.headOption.getOrElse("")}]")
          }
      }


    println("\n")

  }

  def filterRet(words: java.util.List[Token]): Unit = {

    words.asScala.to(LazyList)
      .filter { t => !filterSkipType.contains(t.getPartOfSpeechLevel1) }
      //      .filter { t => t.getPartOfSpeechLevel1 != "動詞" }
      //      .groupBy(t => t.getPartOfSpeechLevel1)
      .foreach { t =>

        print(s"${t.getSurface}(${t.getPartOfSpeechLevel1}) ")

      }


    println("\n")

  }

}
