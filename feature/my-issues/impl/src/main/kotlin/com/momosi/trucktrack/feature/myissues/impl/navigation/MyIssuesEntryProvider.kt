package com.momosi.trucktrack.feature.myissues.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.momosi.trucktrack.feature.myissues.api.MyIssuesNavKey
import com.momosi.trucktrack.feature.myissues.impl.MyIssuesScreen

fun EntryProviderScope<NavKey>.myIssuesEntries() {
    entry<MyIssuesNavKey> {
        MyIssuesScreen()
    }
}

