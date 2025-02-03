package net.algostudio.todolist.utils

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


class StickyHeaderDecoration<VB: ViewBinding>(
    private val listener: StickyHeaderInterface<VB>, root: View
) : RecyclerView.ItemDecoration() {

    private var mStickyHeaderHeight = 0

    private val headerBinding: VB by lazy {
        listener.getBindingInflater().invoke(LayoutInflater.from(root.context))
    }

    private val headerView get() = headerBinding.root

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)

        val topChild = parent.getChildAt(0)
        parent.getChildAdapterPosition(topChild)
            .also { topChildPosition ->
                val headerPosition = listener.getHeaderForPosition(topChildPosition)
                listener.bindHeaderData(headerBinding, headerPosition)
                layoutHeaderView(parent)
                val contactPoint = headerView.bottom
                val childInContact = getChildInContact(parent, contactPoint, headerPosition)

                if (childInContact != null && listener.isHeader(
                        parent.getChildAdapterPosition(
                            childInContact
                        )
                    )
                ) {
                    moveHeader(canvas, headerView, childInContact)
                    return
                }
                drawHeaderView(canvas)
            }
    }

    private fun drawHeaderView(canvas: Canvas) {
        canvas.save()
        canvas.translate(0f, 0f)
        headerView.draw(canvas)
        canvas.restore()
    }

    private fun layoutHeaderView(parent: RecyclerView) {
        headerView.measure(
            View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)
        )
        headerView.layout(0, 0, headerView.measuredWidth, headerView.measuredHeight.also { mStickyHeaderHeight = it })
    }

    private fun getChildInContact(
        parent: RecyclerView,
        contactPoint: Int,
        currentHeaderPos: Int
    ): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            var heightTolerance = 0
            val child = parent.getChildAt(i)

            //measure height tolerance with child if child is another header
            if (currentHeaderPos != i) {
                val isChildHeader: Boolean =
                    listener.isHeader(parent.getChildAdapterPosition(child))
                if (isChildHeader) {
                    heightTolerance = mStickyHeaderHeight - child.height
                }
            }

            //add heightTolerance if child top be in display area
            val childBottomPosition = if (child.top > 0) {
                child.bottom + heightTolerance
            } else {
                child.bottom
            }

            if (childBottomPosition > contactPoint) {
                if (child.top <= contactPoint) {
                    // This child overlaps the contactPoint
                    childInContact = child
                    break
                }
            }
        }
        return childInContact
    }

    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
        c.save()
        c.translate(0f, (nextHeader.top - currentHeader.height).toFloat())
        headerView.draw(c)
        c.restore()
    }
}