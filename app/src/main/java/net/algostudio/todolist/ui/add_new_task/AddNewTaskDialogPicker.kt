package net.algostudio.todolist.ui.add_new_task

import android.content.Context

interface AddNewTaskDialogPicker {
    fun showTimerPickerDialog(
        context: Context,
        onCancelListener: () -> Unit,
        onTimePicked: (String) -> Unit
    )
    fun showDatePickerDialog(context: Context, onDateTimePicked: (Long) -> Unit)
}