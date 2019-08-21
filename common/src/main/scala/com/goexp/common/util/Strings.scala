package com.goexp.common.util

object Strings {
  def isEmpty(str: String): Boolean = str == null || str.isEmpty

  def isNotEmpty(str: String): Boolean = str != null && !str.isEmpty
}