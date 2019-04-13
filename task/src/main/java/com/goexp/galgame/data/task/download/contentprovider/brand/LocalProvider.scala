package com.goexp.galgame.data.task.download.contentprovider.brand

import java.io.IOException
import java.nio.file.Files

import com.goexp.galgame.data.Config
import com.goexp.galgame.data.model.Game
import com.goexp.galgame.data.task.client.GetChu

object LocalProvider {
  def getList(id: Int): List[Game] = {
    val path = Config.BRAND_CACHE_ROOT.resolve(s"$id.bytes")
    try {

      val bytes = Files.readAllBytes(path)

      GetChu.BrandService.gamesFrom(bytes)
    } catch {
      case e: IOException =>
        e.printStackTrace()
        throw new RuntimeException(e)
    }
  }
}