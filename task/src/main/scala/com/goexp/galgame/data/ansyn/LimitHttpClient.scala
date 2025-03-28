package com.goexp.galgame.data.ansyn

import com.goexp.common.util.Logger
import com.goexp.common.util.web.HttpUtil
import com.goexp.galgame.common.util.SpClient

import java.net.http.HttpResponse.BodyHandlers
import java.net.http.{HttpClient, HttpRequest, HttpResponse}
import java.net.{ProxySelector, URI}
import java.time.Duration
import java.util.concurrent.{CompletableFuture, Executors, TimeUnit}
import scala.concurrent.{ExecutionContext, Future}



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

  var downTaskCount = 0

  private def delay() = {
    downTaskCount += 1

    TimeUnit.SECONDS.sleep(1)

    if (downTaskCount % limits == 0) {
      logger.debug("sleep")
      try
        unit.sleep(waitTime)
        //catch that ex not unlock
      catch {
        case _: InterruptedException =>
      }
    }
  }

  def sendAsync[T](request: HttpRequest,
                   responseBodyHandler: HttpResponse.BodyHandler[T]): CompletableFuture[HttpResponse[T]] = {
    this.synchronized {
      delay()
      logger.debug(s"Sending[${downTaskCount}] ${request.uri()}")
    }


    SpClient.httpClient.sendAsync(request, responseBodyHandler)
  }
}

object LimitHttpClient {

  //default
  val client = new LimitHttpClient(30, 20, TimeUnit.SECONDS)

  def apply(): LimitHttpClient = {
    client
  }


  //test
  def main(args: Array[String]): Unit = {

    val request = HttpRequest.newBuilder.uri(URI.create("http://localhost")).build()

    Range(1, 100).foreach {
      _ =>
        Future {
          LimitHttpClient().sendAsync(request, BodyHandlers.discarding()).whenComplete((r, e) =>
            println("OK"))

        }(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(30)))

    }

    TimeUnit.MINUTES.sleep(20)
  }
}


