package net.algostudio.todolist.data.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import net.algostudio.todolist.domain.model.TaskEntity
import net.algostudio.todolist.domain.model.TodoListUiModel

interface TaskRepository {
    fun addTask(taskEntity: TaskEntity)
    fun deleteTask(taskEntity: TaskEntity)
    fun getListTask(): LiveData<MutableList<TodoListUiModel>>
}