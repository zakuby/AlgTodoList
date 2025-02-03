package net.algostudio.todolist.data.repository

import androidx.lifecycle.map
import net.algostudio.todolist.data.localsource.TaskDummyDataSource
import net.algostudio.todolist.domain.model.TaskEntity
import net.algostudio.todolist.domain.model.TodoListUiModel
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val localSource: TaskDummyDataSource
) : TaskRepository {
    override fun addTask(taskEntity: TaskEntity) = localSource.insert(taskEntity)

    override fun deleteTask(taskEntity: TaskEntity) = localSource.delete(taskEntity)

    override fun getListTask() = localSource.getAllTask().map { taskList ->
        val groupedTask = taskList.sortedBy { it.date }.groupBy { it.date }
        val todoList = mutableListOf<TodoListUiModel>()
        groupedTask.keys.forEach { key ->
            todoList.add(TodoListUiModel.HeaderDate(key))
            todoList.addAll(
                groupedTask[key]?.map {
                    TodoListUiModel.ContentTask(it)
                } ?: emptyList()
            )
        }
        todoList
    }
}