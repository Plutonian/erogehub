package entity.group

import com.goexp.galgame.common.model.brand.CommonBrand

import scala.beans.BeanProperty

case class BrandItem(
                      @BeanProperty title: String,
                      @BeanProperty count: Int,
                      @BeanProperty comp: String,
                      @BeanProperty brand: CommonBrand,
                      @BeanProperty children: Array[BrandItem]

                    ) extends DataItem
