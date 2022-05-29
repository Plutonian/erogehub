package api

import com.fasterxml.jackson.databind.DeserializationFeature._
import com.fasterxml.jackson.databind.SerializationFeature._

import com.fasterxml.jackson.databind.ObjectMapper
import play.libs.Json

object Config {

  def init() = {
    val mapper = new ObjectMapper()
      .disable(FAIL_ON_UNKNOWN_PROPERTIES)
//      .disable(READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
//      .disable(ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
      .disable(WRITE_DATES_AS_TIMESTAMPS)
//      .enable(ACCEPT_SINGLE_VALUE_AS_ARRAY)
//      .enable(UNWRAP_SINGLE_VALUE_ARRAYS)

    // Needs to set to Json helper
    Json.setObjectMapper(mapper)
  }

}
