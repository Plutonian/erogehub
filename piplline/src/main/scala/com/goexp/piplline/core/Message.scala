package com.goexp.piplline.core

private[piplline]
case class Message(target: Class[_ <: MessageHandler], entity: Any)