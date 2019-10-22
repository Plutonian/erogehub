package com.goexp.galgame.gui.task

import java.util.Objects

import javafx.beans.value.WeakChangeListener
import javafx.concurrent.{Service, Task}

class TaskService[V](val taskCreator: () => Task[V]) extends Service[V] {
  Objects.requireNonNull(taskCreator)

  this.exceptionProperty.addListener {
    new WeakChangeListener[Throwable]((_, _, newValue) =>
      if (newValue != null)
        newValue.printStackTrace()
    )
  }

  override def createTask: Task[V] = taskCreator()
}

object TaskService {
  def apply[V](taskCreator: () => Task[V]) = new TaskService[V](taskCreator)
}