package net.algostudio.todolist.data.localsource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.algostudio.todolist.domain.model.TaskEntity
import net.algostudio.todolist.utils.getTodayDate
import java.util.Calendar

class TaskDummyDataSourceImpl : TaskDummyDataSource {

    private val calendar by lazy { Calendar.getInstance() }

    private var dummyListTask = mutableListOf(
        TaskEntity(
            title = "Stand up meeting",
            date = calendar.getTodayDate(),
            time = "08:30"
        ),
        TaskEntity(
            title = "Register UI",
            date = calendar.getTodayDate(),
            time = "09:00"
        ),
        TaskEntity(
            title = "Retrospective Meeting",
            date = calendar.getTodayDate(),
            time = "09.30"
        ),
        TaskEntity(
            title = "To do List Mockup",
            date = calendar.getTodayDate(),
            time = "10.00"
        ),
        TaskEntity(
            title = "Stand up meeting",
            date = calendar.getTodayDate(),
            time = "10.30"
        )
    )

    private var _listTaskLiveData = MutableLiveData<List<TaskEntity>>().apply {
        value = dummyListTask
    }


    private fun refreshDummyLiveData() {
        _listTaskLiveData.value = dummyListTask
    }

    override fun insert(taskEntity: TaskEntity) {
        dummyListTask.add(taskEntity)
        refreshDummyLiveData()
    }

    override fun delete(taskEntity: TaskEntity) {
        dummyListTask.remove(taskEntity)
        refreshDummyLiveData()
    }

    override fun getAllTask(): LiveData<List<TaskEntity>> = _listTaskLiveData
}