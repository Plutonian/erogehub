package com.goexp.galgame.gui.model

import com.goexp.galgame.common.model.CommonBrand
import java.util.StringJoiner

class Brand extends CommonBrand {
  override def toString: String = new StringJoiner(", ", classOf[Brand].getSimpleName + "[", "]").add("id=" + id).add("name='" + name + "'").add("website='" + website + "'").add("comp='" + comp + "'").add("isLike=" + isLike).toString

  //    @Override
  //    public String toString() {
  //        return super.toString() + "Brand{" +
  //                "isLike=" + isLike +
  //                "} ";
  //    }
}