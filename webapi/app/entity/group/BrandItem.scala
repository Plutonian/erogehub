package entity.group

import com.goexp.galgame.common.model.brand.CommonBrand

case class BrandItem(title: String,
                     count: Int,
                     brand: CommonBrand) extends DataItem
