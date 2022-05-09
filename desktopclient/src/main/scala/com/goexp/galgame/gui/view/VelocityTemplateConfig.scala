package com.goexp.galgame.gui.view

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.runtime.RuntimeConstants
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader

import java.io.StringWriter

object VelocityTemplateConfig {

  case class Tpl(name: String) {

    def process(dataModel: VelocityContext): String = {
      val writer = new StringWriter
      ve.getTemplate(name).merge(dataModel, writer)
      writer.flush()
      writer.toString // n
    }
  }

  val ve = new VelocityEngine

  init()


  def init() = {

    ve.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath")
    ve.setProperty("resource.loader.classpath.class", classOf[ClasspathResourceLoader].getName)
    ve.init

  }


  def tpl(name: String) = {
    Tpl(name)
  }


}
