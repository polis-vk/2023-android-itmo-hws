package ru.ok.itmo.example.chats.recycler

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.R

class ChatsDecorator(context: Context) : RecyclerView.ItemDecoration() {
    private val space = context.resources.getDimensionPixelOffset(R.dimen.space_between_items)
    private val spaceStartEnd = context.resources.getDimensionPixelOffset(R.dimen.space_start_end)
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = space
        outRect.left = spaceStartEnd
        outRect.right = spaceStartEnd
    }
}