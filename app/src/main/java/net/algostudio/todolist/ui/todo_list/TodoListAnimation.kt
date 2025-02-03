package net.algostudio.todolist.ui.todo_list

import androidx.recyclerview.widget.RecyclerView
import net.algostudio.todolist.databinding.FragmentTodoListBinding
import java.lang.ref.WeakReference

interface TodoListAnimation {
    fun initTodoListAnimation(
        binding: WeakReference<FragmentTodoListBinding?>,
        viewModel: WeakReference<TodoListViewModel?>,
        adapter: WeakReference<TodoListAdapter?>
    )

    fun animateRvToNewTask()

    fun animateItemOnDelete(position: Int)

    fun initAnimationTouchHelper(recyclerView: RecyclerView)
}