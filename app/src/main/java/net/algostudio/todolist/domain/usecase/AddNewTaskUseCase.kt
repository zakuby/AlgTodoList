package net.algostudio.todolist.domain.usecase

import net.algostudio.todolist.data.repository.TaskRepository
import net.algostudio.todolist.domain.model.TaskEntity
import javax.inject.Inject

class AddNewTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    fun invoke(taskEntity: TaskEntity) = taskRepository.addTask(taskEntity)
}