package com.goexp.galgame.common.model

import scala.beans.BeanProperty

object Emotion {

  val HATE: Emotion = Emotion("嫌い", -99)
  val UNCHECKED: Emotion = Emotion("普通", 0)
  val HOPE: Emotion = Emotion("気になり", 1)
  val LIKE: Emotion = Emotion("好き", 99)

  lazy val EMOTIONS = Set(
    HATE,
    UNCHECKED,
    HOPE,
    LIKE
  )


  def apply(value: Int) = {
    EMOTIONS.find(_.value == value).get
  }


}

case class Emotion(
                    @BeanProperty name: String,
                    @BeanProperty value: Int
                  )
