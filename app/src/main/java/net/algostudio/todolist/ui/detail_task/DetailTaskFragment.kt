package net.algostudio.todolist.ui.detail_task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import net.algostudio.todolist.R
import net.algostudio.todolist.core.BaseFragment
import net.algostudio.todolist.databinding.FragmentDetailTaskBinding
import net.algostudio.todolist.utils.getAttrColorPrimaryHex
import net.algostudio.todolist.utils.setHTMLText
import net.algostudio.todolist.utils.toFormatDate

class DetailTaskFragment : BaseFragment<FragmentDetailTaskBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDetailTaskBinding
        get() = FragmentDetailTaskBinding::inflate

    private val args by navArgs<DetailTaskFragmentArgs>()

    override fun FragmentDetailTaskBinding.initBinding() {
        with(args.taskEntity){
            tvDetailTitle.text = title

            tvDetailDesc.text = description.ifEmpty { "Description task is empty." }
            tvDetailDatePicked.text = date.toFormatDate()
            ivDetailTimePicked.isVisible = time.isNotBlank()
            tvDetailTimePicked.apply {
                val colorPrimaryHex = requireContext().getAttrColorPrimaryHex()
                isVisible = time.isNotBlank()
                setHTMLText(context.getString(R.string.time_picked, colorPrimaryHex, time))
            }
        }
    }
}