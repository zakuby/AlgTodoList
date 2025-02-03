package net.algostudio.todolist.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskEntity(
    val title: String,
    var date: Long,
    val description: String = "",
    val time: String = "",
    var isChecked: Boolean = false,
    var showDelete: Boolean = false
) : Parcelable