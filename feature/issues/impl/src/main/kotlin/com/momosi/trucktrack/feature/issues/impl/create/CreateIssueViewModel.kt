package com.momosi.trucktrack.feature.issues.impl.create

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momosi.trucktrack.core.common.coroutines.DispatcherProvider
import com.momosi.trucktrack.core.issue.IssueAttachmentRepository
import com.momosi.trucktrack.core.issue.IssueRepository
import com.momosi.trucktrack.core.issue.model.IssueCreate
import com.momosi.trucktrack.core.vehicle.VehicleRepository
import com.momosi.trucktrack.core.vehicle.model.Vehicle
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.io.path.createTempFile

@HiltViewModel
class CreateIssueViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val vehicleRepository: VehicleRepository,
    private val issueRepository: IssueRepository,
    private val issueAttachmentRepository: IssueAttachmentRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _state = MutableStateFlow(CreateIssueState())
    val state: StateFlow<CreateIssueState> = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CreateIssueState(),
    )

    private val _events = Channel<CreateIssueEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadVehicles()
    }

    fun onAction(action: CreateIssueAction) {
        when (action) {
            is CreateIssueAction.SelectVehicle -> selectVehicle(action.vehicle)
            is CreateIssueAction.UpdateTitle -> _state.update { it.copy(title = action.title) }
            is CreateIssueAction.UpdateDescription -> _state.update { it.copy(description = action.description) }
            is CreateIssueAction.SelectPriority -> _state.update { it.copy(selectedPriority = action.priority) }
            is CreateIssueAction.AddPhotos -> addPhotos(action.uris)
            is CreateIssueAction.RemovePhoto -> removePhoto(action.uri)
            is CreateIssueAction.Submit -> submit()
            is CreateIssueAction.ToggleVehicleDropdown -> _state.update { it.copy(vehicleDropdownExpanded = !it.vehicleDropdownExpanded) }
        }
    }

    private fun loadVehicles() {
        viewModelScope.launch {
            vehicleRepository.getVehicles()
                .onSuccess { vehicles ->
                    _state.update { it.copy(vehicles = VehiclesContent.Loaded(vehicles.toImmutableList())) }
                }
                .onFailure {
                    _state.update { it.copy(vehicles = VehiclesContent.Error) }
                }
        }
    }

    private fun selectVehicle(vehicle: Vehicle) {
        _state.update { it.copy(selectedVehicle = vehicle, vehicleDropdownExpanded = false) }
    }

    private fun addPhotos(uris: List<Uri>) {
        _state.update { current ->
            val combined = (current.photoUris + uris).distinct().toImmutableList()
            current.copy(photoUris = combined)
        }
    }

    private fun removePhoto(uri: Uri) {
        _state.update { current ->
            current.copy(photoUris = current.photoUris.filter { it != uri }.toImmutableList())
        }
    }

    private fun submit() {
        val current = _state.value
        val vehicle = current.selectedVehicle ?: return
        if (current.isSubmitting) return

        _state.update { it.copy(isSubmitting = true) }

        viewModelScope.launch {
            issueRepository.createIssue(
                IssueCreate(
                    vehicleId = vehicle.id,
                    title = current.title,
                    description = current.description,
                    priority = current.selectedPriority,
                ),
            )
                .onSuccess { issue ->
                    uploadPhotos(issue.id, current.photoUris)
                    _events.send(CreateIssueEvent.IssueCreated(issue.id))
                }
                .onFailure {
                    _state.update { it.copy(isSubmitting = false) }
                    _events.send(CreateIssueEvent.CreationFailed)
                }
        }
    }

    private suspend fun uploadPhotos(issueId: Long, uris: List<Uri>) {
        coroutineScope {
            uris.forEach { uri ->
                launch {
                    val tempFile = withContext(dispatcherProvider.io()) {
                        val file = createTempFile("upload", ".jpg").toFile()
                        context.contentResolver.openInputStream(uri)?.use { input ->
                            file.outputStream().use { output -> input.copyTo(output) }
                        }
                        file
                    }
                    try {
                        val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
                        issueAttachmentRepository.uploadPhoto(issueId, tempFile, mimeType)
                    } finally {
                        tempFile.delete()
                    }
                }
            }
        }
    }
}
