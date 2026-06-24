package com.momosi.trucktrack.core.issue.di

import com.momosi.trucktrack.core.issue.IssueAttachmentRepository
import com.momosi.trucktrack.core.issue.IssueAttachmentRepositoryImpl
import com.momosi.trucktrack.core.issue.IssueRepository
import com.momosi.trucktrack.core.issue.IssueRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
}
