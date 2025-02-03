package net.algostudio.todolist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.algostudio.todolist.data.localsource.TaskDummyDataSource
import net.algostudio.todolist.data.repository.TaskRepository
import net.algostudio.todolist.data.repository.TaskRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    fun provideTaskRepository(
        taskDummyDataSource: TaskDummyDataSource
    ): TaskRepository = TaskRepositoryImpl(taskDummyDataSource)
}