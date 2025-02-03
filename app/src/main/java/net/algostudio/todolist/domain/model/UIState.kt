package net.algostudio.todolist.domain.model

sealed class UiState<out V> {
    class OnLoading<V> : UiState<V>()
    data class OnSuccess<V : Any>(val data: V? = null) : UiState<V>()
    data class OnFailed<V>(val message: String? = null, ) : UiState<V>()
}