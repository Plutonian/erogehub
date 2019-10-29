package com.goexp.galgame.data.source.erogamescape.model

class Game extends com.goexp.galgame.data.model.Game {
  var middle: String = _
  var pian: String = _


  override def toString = s"${super.toString} Game($middle, $pian)"
}
