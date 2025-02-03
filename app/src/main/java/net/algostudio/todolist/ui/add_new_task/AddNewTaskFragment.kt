package net.algostudio.todolist.ui.add_new_task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.algostudio.todolist.R
import net.algostudio.todolist.core.BaseFragment
import net.algostudio.todolist.databinding.FragmentAddNewTaskBinding
import net.algostudio.todolist.utils.getAttrColorPrimaryHex
import net.algostudio.todolist.utils.getTodayDate
import net.algostudio.todolist.utils.setHTMLText
import net.algostudio.todolist.utils.toFormatDate
import java.util.Calendar


@AndroidEntryPoint
class AddNewTaskFragment : BaseFragment<FragmentAddNewTaskBinding>() {

    companion object {
        const val REQUEST_KEY = "request_add_new_task"
        const val KEY_NEW_TASK_ID = "key_add_new_task_id"
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddNewTaskBinding
        get() = FragmentAddNewTaskBinding::inflate

    private val viewModel by viewModels<AddNewTaskViewModel>()

    override fun FragmentAddNewTaskBinding.initBinding() {
        editTextInputTitleTask.addTextChangedListener { text ->
            val title = text.toString()
            if (title.lowercase().contains("today")){
                val todayTimeMillis = Calendar.getInstance().getTodayDate()
                onDateTimePicked(todayTimeMillis)
            } else if (viewModel.isCurrentDatePickedIsToday()) {
                onDateTimePicked(0L)
            }
            viewModel.updateTitle(text.toString())
        }
        editTextInputDescriptionTask.addTextChangedListener { text ->
            viewModel.updateDescription(text.toString())
        }
        switchTimePicker.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showTimerPickerDialog()
            } else {
                binding?.tvTimePicked?.text = getString(R.string.time)
                viewModel.updateTimePicked("")
            }
        }

        editTextSelectDate.setOnClickListener { showDatePickerDialog() }
        buttonCancel.setOnClickListener { findNavController().popBackStack() }
        buttonSave.setOnClickListener {
            viewModel.addNewTask()
            setFragmentResult(
                REQUEST_KEY,
                bundleOf(
                    KEY_NEW_TASK_ID to viewModel.currentTask.value
                )
            )
            findNavController().popBackStack()
        }
    }

    private fun showDatePickerDialog(){
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, year, month, dayOfMonth ->
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

    private fun onDateTimePicked(pickedDateTimeMillis: Long){
        if (pickedDateTimeMillis == 0L) {
            binding?.editTextSelectDate?.text?.clear()
        } else {
            binding?.editTextSelectDate?.setText(pickedDateTimeMillis.toFormatDate())
        }

        viewModel.updateDatePicked(pickedDateTimeMillis)
    }

    private fun showTimerPickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val myTimeListener =
            OnTimeSetListener { view, hourOfDay, minuteListener ->
                if (view.isShown) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minuteListener)
                    val actualHour = if (hourOfDay < 10) "0$hourOfDay" else hourOfDay
                    val actualMinute = if (minuteListener < 10) "0$minuteListener" else minuteListener
                    val time = "$actualHour:$actualMinute"
                    viewModel.updateTimePicked(time)
                    val colorPrimaryHex = requireContext().getAttrColorPrimaryHex()
                    binding?.tvTimePicked?.setHTMLText(
                        requireContext().getString(
                            R.string.time_picked,
                            colorPrimaryHex,
                            time
                        )
                    )
                }
            }
        TimePickerDialog(
            requireContext(),
            R.style.DatePickerTheme,
            myTimeListener,
            hour,
            minute,
            true
        ).apply {
            setTitle(getString(R.string.set_time))
            setOnCancelListener {
                binding?.switchTimePicker?.isChecked = false
            }
            show()
        }
    }

    override fun initObserver() {
        super.initObserver()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isFormValid().observe(viewLifecycleOwner) {
                binding?.buttonSave?.isEnabled = it
            }
        }
    }
}