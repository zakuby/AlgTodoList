package net.algostudio.todolist.ui.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.algostudio.todolist.domain.model.TaskEntity
import net.algostudio.todolist.domain.usecase.TodoListUseCase
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val useCase: TodoListUseCase
) : ViewModel() {

    private var _newTaskEntity: TaskEntity? = null

    val newTaskEntity get() = _newTaskEntity

    fun setNewTaskEntity(taskEntity: TaskEntity?){
        _newTaskEntity = taskEntity
    }

    fun getAllTask() = useCase.getListTask()

    fun deleteTask(taskEntity: TaskEntity) = viewModelScope.launch {
        delay(500)
        useCase.deleteTask(taskEntity)
    }
}