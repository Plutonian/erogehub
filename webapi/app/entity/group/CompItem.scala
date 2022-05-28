package entity.group

import com.goexp.galgame.common.model.brand.CommonBrand

case class CompItem(title: String,
                    count: Int,
                    comp: String,
                    children: Array[CommonBrand]) extends DataItem
