package com.momosi.trucktrack.feature.issues.impl.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momosi.trucktrack.core.common.io.PhotoData
import com.momosi.trucktrack.core.issue.IssueAttachmentRepository
import com.momosi.trucktrack.core.issue.IssueRepository
import com.momosi.trucktrack.core.issue.model.IssueCreate
import com.momosi.trucktrack.core.vehicle.VehicleRepository
import com.momosi.trucktrack.core.vehicle.model.Vehicle
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateIssueViewModel(private val vehicleRepository: VehicleRepository, private val issueRepository: IssueRepository, private val issueAttachmentRepository: IssueAttachmentRepository) : ViewModel() {

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
            is CreateIssueAction.AddPhotos -> addPhotos(action.files)
            is CreateIssueAction.RemovePhoto -> removePhoto(action.fileName)
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

    private fun addPhotos(files: List<PlatformFile>) {
        viewModelScope.launch {
            val newPhotos = files.map { file ->
                PhotoData(
                    bytes = file.readBytes(),
                    fileName = file.name,
                    mimeType = mimeTypeFromFileName(file.name),
                )
            }
            _state.update { current ->
                val combined = (current.photos + newPhotos)
                    .distinctBy { it.fileName }
                    .toImmutableList()
                current.copy(photos = combined)
            }
        }
    }

    private fun removePhoto(fileName: String) {
        _state.update { current ->
            current.copy(photos = current.photos.filter { it.fileName != fileName }.toImmutableList())
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
                    uploadPhotos(issue.id, current.photos)
                    _events.send(CreateIssueEvent.IssueCreated(issue.id))
                }
                .onFailure {
                    _state.update { it.copy(isSubmitting = false) }
                    _events.send(CreateIssueEvent.CreationFailed)
                }
        }
    }

    private suspend fun uploadPhotos(issueId: Long, photos: List<PhotoData>) {
        coroutineScope {
            photos.forEach { photo ->
                launch {
                    issueAttachmentRepository.uploadPhoto(
                        issueId = issueId,
                        fileName = photo.fileName,
                        fileBytes = photo.bytes,
                        contentType = photo.mimeType,
                    )
                }
            }
        }
    }

    private fun mimeTypeFromFileName(fileName: String): String = when (
        fileName.substringAfterLast('.', "").lowercase()
    ) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "gif" -> "image/gif"
        "webp" -> "image/webp"
        "heic", "heif" -> "image/heic"
        else -> "image/jpeg"
    }
}
