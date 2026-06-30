package com.momosi.trucktrack.feature.issues.impl.create

import com.momosi.trucktrack.core.issue.model.IssuePriority
import com.momosi.trucktrack.core.vehicle.model.Vehicle

sealed interface CreateIssueAction {
    data class SelectVehicle(val vehicle: Vehicle) : CreateIssueAction
    data class UpdateTitle(val title: String) : CreateIssueAction
    data class UpdateDescription(val description: String) : CreateIssueAction
    data class SelectPriority(val priority: IssuePriority) : CreateIssueAction
    data class AddPhotos(val uris: List<String>) : CreateIssueAction
    data class RemovePhoto(val uri: String) : CreateIssueAction
    data object Submit : CreateIssueAction
    data object ToggleVehicleDropdown : CreateIssueAction
}
