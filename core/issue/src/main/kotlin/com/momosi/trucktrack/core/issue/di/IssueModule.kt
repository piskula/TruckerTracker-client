package com.momosi.trucktrack.core.issue.di

import com.momosi.trucktrack.core.issue.IssueAttachmentRepository
import com.momosi.trucktrack.core.issue.IssueAttachmentRepositoryImpl
import com.momosi.trucktrack.core.issue.IssueRepository
import com.momosi.trucktrack.core.issue.IssueRepositoryImpl
import com.momosi.trucktrack.core.issue.api.IssueApi
import com.momosi.trucktrack.core.issue.api.IssueAttachmentApi
import com.momosi.trucktrack.core.issue.api.IssueHistoryApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class IssueModule {

    @Binds
    @Singleton
    abstract fun bindIssueRepository(impl: IssueRepositoryImpl): IssueRepository

    @Binds
    @Singleton
    abstract fun bindIssueAttachmentRepository(impl: IssueAttachmentRepositoryImpl): IssueAttachmentRepository

    companion object {

        @Provides
        @Singleton
        fun provideIssueApi(retrofit: Retrofit): IssueApi =
            retrofit.create(IssueApi::class.java)

        @Provides
        @Singleton
        fun provideIssueAttachmentApi(retrofit: Retrofit): IssueAttachmentApi =
            retrofit.create(IssueAttachmentApi::class.java)

        @Provides
        @Singleton
        fun provideIssueHistoryApi(retrofit: Retrofit): IssueHistoryApi =
            retrofit.create(IssueHistoryApi::class.java)
    }
}

