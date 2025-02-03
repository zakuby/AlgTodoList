package net.algostudio.todolist.ui.todo_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.algostudio.todolist.core.BaseFragment
import net.algostudio.todolist.databinding.FragmentTodoListBinding
import net.algostudio.todolist.domain.model.TaskEntity
import net.algostudio.todolist.domain.model.TodoListUiModel
import net.algostudio.todolist.ui.add_new_task.AddNewTaskFragment
import net.algostudio.todolist.ui.dialog.DeleteConfirmationDialog
import net.algostudio.todolist.utils.StickyHeaderDecoration
import net.algostudio.todolist.utils.getParcelableExt
import java.lang.ref.WeakReference

@AndroidEntryPoint
class TodoListFragment : BaseFragment<FragmentTodoListBinding>(),
    TodoListAnimation by TodoListAnimationImpl(), TodoListAdapterListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTodoListBinding
        get() = FragmentTodoListBinding::inflate

    private val viewModel by viewModels<TodoListViewModel>()

    private val adapter by lazy { TodoListAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(AddNewTaskFragment.REQUEST_KEY) { _, bundle ->
            val newTaskEntity =
                bundle.getParcelableExt<TaskEntity>(AddNewTaskFragment.KEY_NEW_TASK_ID)
            viewModel.setNewTaskEntity(newTaskEntity)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTodoListAnimation(
            binding = WeakReference(binding),
            viewModel = WeakReference(viewModel),
            adapter = WeakReference(adapter)
        )
    }

    override fun FragmentTodoListBinding.initBinding() {
        rvTodoList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@TodoListFragment.adapter
            addItemDecoration(StickyHeaderDecoration(this@TodoListFragment.adapter, root))
        }

        initAnimationTouchHelper(rvTodoList)
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAllTask().observe(viewLifecycleOwner, ::handleNewListTask)
        }
    }

    private fun handleNewListTask(listTask: List<TodoListUiModel>) {
        adapter.submitItems(listTask)

        val isContentEmpty = listTask.filterIsInstance<TodoListUiModel.ContentTask>().isEmpty()
        binding?.apply {
            rvTodoList.isGone = isContentEmpty
            tvEmpty.isVisible = isContentEmpty
        }

        //Check on newly added items to scroll and show animation
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            delay(50)
            animateRvToNewTask()
        }
    }

    override fun onItemClick(position: Int, taskEntity: TaskEntity) {
        val action =
            TodoListFragmentDirections.actionToDoListFragmentToDetailTaskFragment(taskEntity)
        findNavController().navigate(action)
    }

    override fun onItemDelete(position: Int, taskEntity: TaskEntity) {
        DeleteConfirmationDialog {
            animateItemOnDelete(position)
            viewModel.deleteTask(taskEntity)
        }.show(childFragmentManager, "")

    }
}