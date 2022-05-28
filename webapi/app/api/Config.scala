package api

import com.fasterxml.jackson.databind.DeserializationFeature._
import com.fasterxml.jackson.databind.ObjectMapper
import play.libs.Json

object Config {

  def init() = {
    val mapper = new ObjectMapper()
      .disable(FAIL_ON_UNKNOWN_PROPERTIES)
      .disable(READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
      .disable(ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
      .enable(READ_ENUMS_USING_TO_STRING)

    // Needs to set to Json helper
    Json.setObjectMapper(mapper)
  }

}
