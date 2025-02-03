package net.algostudio.todolist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.algostudio.todolist.data.localsource.TaskDummyDataSource
import net.algostudio.todolist.data.localsource.TaskDummyDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {
    @Provides
    @Singleton
    fun provideTaskDummyDataSource(): TaskDummyDataSource {
        return TaskDummyDataSourceImpl()
    }
}