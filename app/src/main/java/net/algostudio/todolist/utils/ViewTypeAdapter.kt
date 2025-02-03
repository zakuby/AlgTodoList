package net.algostudio.todolist.utils

enum class ViewTypeAdapter(val viewType: Int){
    HEADER(0),
    CONTENT(1);
    companion object {
        fun fromViewType(viewType: Int) : ViewTypeAdapter{
            return ViewTypeAdapter.entries.firstOrNull { it.viewType == viewType } ?: CONTENT
        }
    }
}