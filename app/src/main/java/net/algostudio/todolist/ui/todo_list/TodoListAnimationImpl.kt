package net.algostudio.todolist.ui.todo_list

import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.algostudio.todolist.R
import net.algostudio.todolist.databinding.FragmentTodoListBinding
import net.algostudio.todolist.domain.model.TodoListUiModel
import net.algostudio.todolist.utils.ViewTypeAdapter
import java.lang.ref.WeakReference

class TodoListAnimationImpl : TodoListAnimation {
    private var binding: WeakReference<FragmentTodoListBinding?> = WeakReference(null)
    private var viewModel: WeakReference<TodoListViewModel?> = WeakReference(null)
    private var adapter: WeakReference<TodoListAdapter?> = WeakReference(null)

    override fun initTodoListAnimation(
        binding: WeakReference<FragmentTodoListBinding?>,
        viewModel: WeakReference<TodoListViewModel?>,
        adapter: WeakReference<TodoListAdapter?>
    ) {
        this.binding = binding
        this.viewModel = viewModel
        this.adapter = adapter
    }

    override fun initAnimationTouchHelper(recyclerView: RecyclerView) {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return if (viewHolder.itemView.tag == ViewTypeAdapter.HEADER.name)
                    0
                else
                    makeMovementFlags(
                        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                        ItemTouchHelper.START
                    )
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val draggedItemIndex = viewHolder.adapterPosition
                val targetIndex = target.adapterPosition
                if (targetIndex != 0) {
                    adapter.get()?.swapItems(draggedItemIndex, targetIndex)
                    adapter.get()?.getHeaderForPosition(targetIndex)?.let { headerPosition ->
                        val contentTask = adapter.get()?.getItem(targetIndex) as? TodoListUiModel.ContentTask
                        val taskEntity = contentTask?.taskEntity
                        val newHeader = adapter.get()?.getItem(headerPosition) as? TodoListUiModel.HeaderDate
                        taskEntity?.apply {
                            date = newHeader?.date ?: date
                        }
                    }
                }
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.START -> {
                        adapter.get()?.run {
                            getItem(viewHolder.adapterPosition)?.let {
                                val item = it as? TodoListUiModel.ContentTask ?: return@run
                                item.taskEntity.showDelete = !item.taskEntity.showDelete
                                adapter.get()?.notifyItemChanged(viewHolder.adapterPosition)
                            }
                        }
                    }
                }
            }

        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun animateRvToNewTask() {
        viewModel.get()?.newTaskEntity?.let { newTaskEntity ->
            adapter.get()?.findItemPosition { item ->
                item is TodoListUiModel.ContentTask && item.taskEntity == newTaskEntity
            }?.let { newItemPosition ->
                val recyclerView = binding.get()?.rvTodoList
                val layoutManager = recyclerView?.layoutManager as? LinearLayoutManager
                layoutManager?.run {
                    scrollToPositionWithOffset(newItemPosition, 72)
                    findViewByPosition(newItemPosition)?.run {
                        startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
                    }
                }
                viewModel.get()?.setNewTaskEntity(null)
            }
        }
    }

    override fun animateItemOnDelete(position: Int) {
        binding.get()?.rvTodoList?.getChildAt(position)?.run {
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
        }
    }
}