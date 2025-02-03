package net.algostudio.todolist.ui.add_new_task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import net.algostudio.todolist.R
import java.util.Calendar

class AddNewTaskDialogPickerImpl : AddNewTaskDialogPicker {
    override fun showTimerPickerDialog(
        context: Context,
        onCancelListener: () -> Unit,
        onTimePicked: (String) -> Unit
    ) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val myTimeListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minuteListener ->
                if (view.isShown) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minuteListener)
                    val actualHour = if (hourOfDay < 10) "0$hourOfDay" else hourOfDay
                    val actualMinute =
                        if (minuteListener < 10) "0$minuteListener" else minuteListener
                    val time = "$actualHour:$actualMinute"
                    onTimePicked(time)
                }
            }
        TimePickerDialog(
            context,
            R.style.DatePickerTheme,
            myTimeListener,
            hour,
            minute,
            true
        ).apply {
            setTitle(context.getString(R.string.set_time))
            setOnCancelListener { onCancelListener() }
            show()
        }
    }

    override fun showDatePickerDialog(context: Context, onDateTimePicked: (Long) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context, { _, year, month, dayOfMonth ->
                calendar.apply {
                    set(year, month, dayOfMonth, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                onDateTimePicked(calendar.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}