package net.algostudio.todolist.utils

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding


interface StickyHeaderInterface<VB : ViewBinding> {
    /**
     * This method gets called by [StickyHeaderInterface] to fetch the position of the header item in the adapter
     * that is used for (represents) item at specified position.
     * @param itemPosition int. Adapter's position of the item for which to do the search of the position of the header item.
     * @return int. Position of the header item in the adapter.
     */
    fun getHeaderForPosition(itemPosition: Int): Int

    /**
     * This method gets called by [StickyHeaderInterface] to verify whether the item represents a header.
     * @param itemPosition int.
     * @return true, if item at the specified adapter's position represents a header.
     */
    fun isHeader(itemPosition: Int): Boolean
    fun getBindingInflater(): (LayoutInflater) -> VB
    fun bindHeaderData(header: VB, position: Int)
}