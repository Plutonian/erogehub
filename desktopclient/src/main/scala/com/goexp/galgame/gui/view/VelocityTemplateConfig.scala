package com.goexp.galgame.gui.view

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.runtime.RuntimeConstants
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader

import java.io.StringWriter
import scala.util.chaining.scalaUtilChainingOps

object VelocityTemplateConfig {

  case class Tpl(name: String) {

    def process(dataModel: VelocityContext): String = {
      val writer = new StringWriter
      ve.getTemplate(name).merge(dataModel, writer)
      writer.flush()
      writer.toString // n
    }

  }

  import freemarker.template.{Configuration, TemplateExceptionHandler}

  // Create your Configuration instance, and specify if up to what FreeMarker// Create your Configuration instance, and specify if up to what FreeMarker

  // version (here 2.3.22) do you want to apply the fixes that are not 100%
  // backward-compatible. See the Configuration JavaDoc for details.
  //  val cfg = new Configuration(Configuration.VERSION_2_3_31)

  val ve = new VelocityEngine

  init()


  def init() = {

    ve.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath")
    ve.setProperty("resource.loader.classpath.class", classOf[ClasspathResourceLoader].getName)
    ve.init

  }

  def ok() = {
    // 获取模板文件
    val t = ve.getTemplate("/com/goexp/galgame/gui/view/css.css")
    // 设置变量

    val ctx = new VelocityContext
    //    ctx.put("name", "Velocity")
    //    val list =  List(1,2)
    //    ctx.put("list", list)
    // 输出
    val sw = new StringWriter
    t.merge(ctx, sw)
    System.out.println(sw.toString)
  }


  def tpl(name: String) = {
    Tpl(name)
  }


}
