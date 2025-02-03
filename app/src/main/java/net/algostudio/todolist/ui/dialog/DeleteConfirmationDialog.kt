package net.algostudio.todolist.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.algostudio.todolist.R
import net.algostudio.todolist.databinding.DialogDeleteConfirmationBinding

class DeleteConfirmationDialog(
    private val onConfirmDelete: () -> Unit = {}
) : BottomSheetDialogFragment() {

    lateinit var binding: DialogDeleteConfirmationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDeleteConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            buttonCancel.setOnClickListener { dismiss() }
            buttonDelete.setOnClickListener {
                onConfirmDelete()
                dismiss()
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }
}