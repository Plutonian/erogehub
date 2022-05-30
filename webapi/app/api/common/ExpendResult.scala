package api.common

import play.mvc.Result

object ExpendResult {

  implicit class ToJson(ret: Result) {
    def asJson() = {
      ret.as("application/json; charset=utf-8")
    }
  }

}
