package net.algostudio.todolist.ui.todo_list

import net.algostudio.todolist.domain.model.TaskEntity

interface TodoListAdapterListener {
    fun onItemClick(position: Int, taskEntity: TaskEntity)
    fun onItemDelete(position: Int, taskEntity: TaskEntity)
}