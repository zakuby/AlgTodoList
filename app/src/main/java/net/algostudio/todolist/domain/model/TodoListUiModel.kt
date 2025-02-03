package net.algostudio.todolist.domain.model

sealed class TodoListUiModel {
    data class HeaderDate(val date: Long) : TodoListUiModel()
    data class ContentTask(val taskEntity: TaskEntity) : TodoListUiModel()
}