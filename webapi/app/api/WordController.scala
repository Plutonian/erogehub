package api

import api.common.ExpendResult.ToJson
import com.atilika.kuromoji.unidic.Tokenizer
import com.ibm.icu.text.Transliterator
import play.libs.Json
import play.mvc.Controller
import play.mvc.Results.ok

import scala.beans.BeanProperty
import scala.jdk.CollectionConverters._


case class Part(@BeanProperty tokens: java.util.List[Word])

case class Word(
                 @BeanProperty surface: String,
                 @BeanProperty partOfSpeechLevel1: String,
                 @BeanProperty partOfSpeechLevel2: String,
                 @BeanProperty partOfSpeechLevel3: String,
                 @BeanProperty partOfSpeechLevel4: String,
                 @BeanProperty pronunciation: String,
                 @BeanProperty pronunciationBaseForm: String,
                 @BeanProperty writtenForm: String,
                 @BeanProperty writtenBaseForm: String,
                 //                 @BeanProperty hasKanji: Boolean,
                 @BeanProperty allFeatures: String,

               )

object WordController {
  val SkipType = Set("助詞", "助動詞")
}

class WordController extends Controller {


  implicit class japaneseChange(raw: String) {

    val trans = Transliterator.getInstance("Katakana-Hiragana")

    def toひらがな = {

      trans.transliterate(raw)
      //      raw.chars().toArray.map {
      //        case i if (i >= 0x30a1 && i <= 0x30f3) => i - 0x60
      //        case 'ー' => 'ー'
      //        case _ => ' '
      //      }.map(_.toChar).mkString.trim
    }

    def hasKanji() = {
      raw.exists(c => c >= 0x4E00 && c <= 0x9FFF)
    }
  }

  val reg = raw"\n".r


  val tokenizer = new Tokenizer

  def process(request: play.mvc.Http.Request) = {
    val seten = request.body().asText()

    val list = reg.split(seten).map { line =>
      //      println()
      //      println(s">>$line")


      Part(tokenizer.tokenize(line).asScala.map { token =>
        Word(
          token.getSurface,
          token.getPartOfSpeechLevel1,
          token.getPartOfSpeechLevel2,
          token.getPartOfSpeechLevel3,
          token.getPartOfSpeechLevel4,
          {

            if (token.getSurface.hasKanji())
              token.getPronunciation.toひらがな
            else
              ""
          },
          {
            if (token.getSurface.hasKanji())
              token.getPronunciationBaseForm.toひらがな
            else
              ""
          },
          token.getWrittenForm,
          token.getWrittenBaseForm,
          token.getAllFeatures
        )
      }.asJava)
    }

    ok(Json.toJson(Option(list).getOrElse(List().asJava))).asJson()
  }


}
