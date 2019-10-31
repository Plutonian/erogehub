package com.goexp.piplline.core

case class Message(target: Class[_ <: MessageHandler], entity: Any)