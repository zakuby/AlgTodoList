package net.algostudio.todolist.data.localsource

import androidx.lifecycle.LiveData
import net.algostudio.todolist.domain.model.TaskEntity

interface TaskDummyDataSource {
    fun insert(taskEntity: TaskEntity)

    fun delete(taskEntity: TaskEntity)

    fun getAllTask(): LiveData<List<TaskEntity>>
}