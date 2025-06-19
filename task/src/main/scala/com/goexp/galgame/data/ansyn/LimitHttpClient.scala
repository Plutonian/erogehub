package com.goexp.galgame.data.ansyn

import com.goexp.common.util.Logger
import com.goexp.galgame.common.util.SpClient

import java.net.http.{HttpRequest, HttpResponse}
import java.util.concurrent.{CompletableFuture, ThreadLocalRandom, TimeUnit}


/**
 * HttpRequest send limit client
 *
 * count requset send counts,sleep current thread when reach the limit
 *
 * @param limits   times limit
 * @param waitTime wait for time
 * @param unit     wait for time unit
 */
class LimitHttpClient(val limits: Int, val waitTime: Int, val unit: TimeUnit) extends Logger {

  private var downTaskCount = 0

  def sendAsync[T](request: HttpRequest,
                   responseBodyHandler: HttpResponse.BodyHandler[T]): CompletableFuture[HttpResponse[T]] = {
    this.synchronized {
      delay()
      logger.debug(s"Sending[${downTaskCount}] ${request.uri()}")
    }


    SpClient.httpClient.sendAsync(request, responseBodyHandler)

  }

  private def delay() = {
    downTaskCount += 1

    val jitter = ThreadLocalRandom.current().nextInt(-500, 500)
    TimeUnit.MILLISECONDS.sleep(1000 + jitter)

    if (downTaskCount % limits == 0) {
      logger.debug("sleep")
      try {
        val jitter = ThreadLocalRandom.current().nextInt(-10, 10)
        unit.sleep(waitTime + jitter)
        //catch that ex not unlock
      }
      catch {
        case _: InterruptedException =>
      }
    }
  }
}

object LimitHttpClient {

  //default
  val client = new LimitHttpClient(20, 30, TimeUnit.SECONDS)

  def apply(): LimitHttpClient = {
    client
  }

}


