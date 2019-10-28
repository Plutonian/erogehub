package com.goexp.galgame.gui.task.game.panel.group.node

import com.goexp.galgame.gui.model.Brand

class BrandItem(private[this] val title: String,
                private[this] val count: Int,
                val brand: Brand) extends DefaultItem(title, count) {
}