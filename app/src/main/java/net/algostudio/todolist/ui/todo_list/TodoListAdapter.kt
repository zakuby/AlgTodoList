package net.algostudio.todolist.ui.todo_list

import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import net.algostudio.todolist.core.BaseListAdapter
import net.algostudio.todolist.databinding.ItemTodoHeaderBinding
import net.algostudio.todolist.databinding.ItemTodoListBinding
import net.algostudio.todolist.domain.model.TodoListUiModel
import net.algostudio.todolist.utils.StickyHeaderInterface
import net.algostudio.todolist.utils.ViewTypeAdapter
import net.algostudio.todolist.utils.toFormatDate

class TodoListAdapter(
    private val mListener: TodoListAdapterListener
) : BaseListAdapter<TodoListUiModel, TodoListAdapter.TodoViewHolder>(), StickyHeaderInterface<ItemTodoHeaderBinding> {

    private var mLastHeaderPosition = 0

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TodoListUiModel.HeaderDate -> ViewTypeAdapter.HEADER.viewType
            else -> ViewTypeAdapter.CONTENT.viewType
        }
    }

    open inner class TodoViewHolder(binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        open fun onBind(position: Int, todoListUiModel: TodoListUiModel) {}
    }

    inner class HeaderViewHolder(
        private val binding: ItemTodoHeaderBinding
    ) : TodoViewHolder(binding) {
        override fun onBind(position: Int, todoListUiModel: TodoListUiModel) {
            if (todoListUiModel is TodoListUiModel.HeaderDate)
                binding.bind(todoListUiModel)
        }
    }

    fun ItemTodoHeaderBinding.bind(headerDate: TodoListUiModel.HeaderDate) {
        tvHeaderDate.text = headerDate.date.toFormatDate()
    }

    override fun getBindingInflater(): (LayoutInflater) -> ItemTodoHeaderBinding {
        return ItemTodoHeaderBinding::inflate
    }

    override fun bindHeaderData(header: ItemTodoHeaderBinding, position: Int) {
        if (position == -1) return
        val item = getItem(position)
        if (item is TodoListUiModel.HeaderDate) header.bind(item)
    }

    override fun getHeaderForPosition(itemPosition: Int): Int {
        if (itemPosition == -1) return 0
        if (getItem(itemPosition) is TodoListUiModel.HeaderDate) {
            mLastHeaderPosition = itemPosition
            return itemPosition
        } else {
            var loopItemPosition = itemPosition
            while (loopItemPosition >= 0) {
                val item = getItem(loopItemPosition)
                if (item is TodoListUiModel.HeaderDate) {
                    mLastHeaderPosition = loopItemPosition
                    return loopItemPosition
                }
                loopItemPosition -= 1
            }
        }
        return mLastHeaderPosition
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return itemPosition != -1 && getItem(itemPosition) is TodoListUiModel.HeaderDate
    }

    inner class ContentViewHolder(
        private val binding: ItemTodoListBinding
    ) :TodoViewHolder(binding) {
        @SuppressLint("ClickableViewAccessibility")
        override fun onBind(position: Int, todoListUiModel: TodoListUiModel) {
            if (todoListUiModel !is TodoListUiModel.ContentTask) return
            val taskEntity = todoListUiModel.taskEntity
            with(binding) {
                root.apply {
                    setOnLongClickListener {
                        taskEntity.showDelete = true
                        notifyItemChanged(position)
                        return@setOnLongClickListener true
                    }

                    setOnClickListener {
                        mListener.onItemClick(position, taskEntity)
                    }
                }
                buttonDelete.apply {
                    isVisible = taskEntity.showDelete
                    setOnClickListener { mListener.onItemDelete(position, taskEntity) }
                }
                tvTitleTask.text = taskEntity.title
                checkBoxTask.apply {
                    isChecked = taskEntity.isChecked
                    setOnClickListener {
                        taskEntity.isChecked = !taskEntity.isChecked
                        notifyItemChanged(position)
                    }
                }
                tvTimeTask.apply {
                    isVisible = taskEntity.time.isNotBlank()
                    text = taskEntity.time
                }
                tvDescriptionTask.apply {
                    isVisible = taskEntity.description.isNotBlank()
                    text = taskEntity.description
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TodoViewHolder {
        val viewHolder = when (ViewTypeAdapter.fromViewType(viewType)) {
            ViewTypeAdapter.HEADER -> HeaderViewHolder(
                ItemTodoHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            ViewTypeAdapter.CONTENT -> ContentViewHolder(
                ItemTodoListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        return viewHolder.apply {
            itemView.tag = ViewTypeAdapter.fromViewType(viewType).name
        }
    }

    override fun onBindViewHolder(
        holder: TodoViewHolder,
        position: Int,
    ) {
        getItem(position)?.let { data ->
            holder.onBind(position, data)
        }
    }
}