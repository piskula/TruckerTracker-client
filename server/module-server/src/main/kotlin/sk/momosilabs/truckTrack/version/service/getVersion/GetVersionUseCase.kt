package sk.momosilabs.truckTrack.version.service.getVersion

import sk.momosilabs.truckTrack.version.model.BuildInfoModel

interface GetVersionUseCase {

    fun get(): BuildInfoModel
}
