package com.goexp.galgame.data.task.local

import org.atilika.kuromoji.Tokenizer

import scala.collection.JavaConverters._

object CleanGameNameTask {
  def main(args: Array[String]): Unit = {
    val tokenizer = Tokenizer.builder.build

    tokenizer.tokenize("続月花美人 〜桜花の頃〜").asScala.toStream
      .filter(token => token.getBaseForm != null)
      .foreach(token => {
        println(s"${token.getSurfaceForm}  {}  ${token.getPartOfSpeech} {}  ${token.isKnown}")
      })
  }
}