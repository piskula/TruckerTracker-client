package com.momosi.trucktrack.core.issue.di

import com.momosi.trucktrack.core.issue.IssueAttachmentRepository
import com.momosi.trucktrack.core.issue.IssueAttachmentRepositoryImpl
import com.momosi.trucktrack.core.issue.IssueRepository
import com.momosi.trucktrack.core.issue.IssueRepositoryImpl
import com.momosi.trucktrack.core.issue.api.IssueApi
import com.momosi.trucktrack.core.issue.api.IssueAttachmentApi
import com.momosi.trucktrack.core.issue.api.IssueHistoryApi
import org.koin.dsl.module

val issueModule = module {
    single { IssueApi(get()) }
    single { IssueHistoryApi(get()) }
    single { IssueAttachmentApi(get()) }
    single<IssueRepository> { IssueRepositoryImpl(get(), get()) }
    single<IssueAttachmentRepository> { IssueAttachmentRepositoryImpl(get()) }
}
