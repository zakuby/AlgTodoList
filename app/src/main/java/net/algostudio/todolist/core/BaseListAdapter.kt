package net.algostudio.todolist.core

import androidx.recyclerview.widget.RecyclerView
import java.util.Collections

abstract class BaseListAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private var items = mutableListOf<T>()

    override fun getItemCount(): Int = items.size

    fun getItems() = items.toList()

    fun submitItems(items: List<T>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    fun getItem(position: Int): T? = items.getOrNull(position)

    open fun swapItems(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun removeItem(position: Int) {
        this.items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun findItemPosition(predicate: (T) -> Boolean): Int? {
        val item = items.firstOrNull(predicate) ?: return null
        return items.indexOf(item)
    }
}