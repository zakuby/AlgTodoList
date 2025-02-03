package net.algostudio.todolist.domain.usecase

import net.algostudio.todolist.data.repository.TaskRepository
import net.algostudio.todolist.domain.model.TaskEntity
import javax.inject.Inject

class TodoListUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    fun getListTask() = taskRepository.getListTask()
    fun deleteTask(taskEntity: TaskEntity) = taskRepository.deleteTask(taskEntity)
}