package net.algostudio.todolist.ui.add_new_task

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
class AddNewTaskFragment : BaseFragment<FragmentAddNewTaskBinding>(),
    AddNewTaskDialogPicker by AddNewTaskDialogPickerImpl() {

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
            if (title.lowercase().contains("today")) {
                val todayTimeMillis = Calendar.getInstance().getTodayDate()
                onDateTimePicked(todayTimeMillis)
            } else if (viewModel.isCurrentDatePickedIsToday()) {
                onDateTimePicked(0L)
            }
            viewModel.updateTitle(text.toString())
        }
        editTextInputDescriptionTask.addTextChangedListener { viewModel.updateDescription(it.toString())}
        switchTimePicker.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showTimerPickerDialog(
                    requireContext(),
                    onCancelListener = {
                        switchTimePicker.isChecked = false
                    },
                    onTimePicked = { time ->
                        val colorPrimaryHex = requireContext().getAttrColorPrimaryHex()
                        viewModel.updateTimePicked(time)
                        tvTimePicked.setHTMLText(
                            requireContext().getString(
                                R.string.time_picked,
                                colorPrimaryHex,
                                time
                            )
                        )
                    }
                )
            } else {
                tvTimePicked.text = getString(R.string.time)
                viewModel.updateTimePicked("")
            }
        }

        editTextSelectDate.setOnClickListener {
            showDatePickerDialog(
                requireContext(),
                onDateTimePicked = { onDateTimePicked(it) }
            )
        }
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

    private fun onDateTimePicked(pickedDateTimeMillis: Long) {
        if (pickedDateTimeMillis == 0L) {
            binding?.editTextSelectDate?.text?.clear()
        } else {
            binding?.editTextSelectDate?.setText(pickedDateTimeMillis.toFormatDate())
        }

        viewModel.updateDatePicked(pickedDateTimeMillis)
    }

    override fun initObserver() {
        super.initObserver()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.run {
                isFormValid().observe(viewLifecycleOwner) {
                    binding?.buttonSave?.isEnabled = it
                }
            }
        }
    }
}