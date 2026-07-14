package com.momosi.trucktrack.core.navigation

import androidx.navigation3.runtime.NavKey

class Navigator(val navigationState: NavigationState) {

    fun navigate(key: NavKey) {
        navigationState.backStack.add(key)
    }

    fun navigateToRoot(key: NavKey) {
        navigationState.backStack.clear()
        navigationState.backStack.add(key)
    }

    fun goBack() {
        if (navigationState.backStack.size <= 1) return
        navigationState.backStack.removeLastOrNull()
    }
}
