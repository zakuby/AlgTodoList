package net.algostudio.todolist.ui.add_new_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import net.algostudio.todolist.domain.model.TaskEntity
import net.algostudio.todolist.domain.usecase.AddNewTaskUseCase
import net.algostudio.todolist.utils.getTodayDate
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddNewTaskViewModel @Inject constructor(
    private val useCase: AddNewTaskUseCase
) : ViewModel() {

    private val _currentTask = MutableLiveData<TaskEntity?>().apply {
        value = TaskEntity("", 0)
    }

    val currentTask: LiveData<TaskEntity?> = _currentTask

    private fun updateTask(taskEntity: TaskEntity?) {
        _currentTask.value = taskEntity
    }

    fun updateTitle(title: String) {
        updateTask(
            currentTask.value?.copy(
                title = title
            )
        )
    }

    fun updateDescription(description: String) {
        updateTask(
            currentTask.value?.copy(
                description = description
            )
        )
    }

    fun updateDatePicked(dateTimeMillis: Long) {
        updateTask(
            currentTask.value?.copy(
                date = dateTimeMillis
            )
        )
    }

    fun isCurrentDatePickedIsToday() =
        currentTask.value?.date == Calendar.getInstance().getTodayDate()

    fun updateTimePicked(time: String) {
        updateTask(
            currentTask.value?.copy(
                time = time
            )
        )
    }

    fun isFormValid() = _currentTask.map { task ->
        task != null && task.title.isNotEmpty() && task.date != 0L
    }

    fun addNewTask() = currentTask.value?.let { useCase.invoke(it) }
}