package com.momosi.trucktrack.core.common.version

import platform.Foundation.NSBundle

class AppVersionProviderImpl : AppVersionProvider {

    private val infoDictionary = NSBundle.mainBundle.infoDictionary

    override val versionName: String = infoDictionary?.get("CFBundleShortVersionString") as? String ?: "dev"

    override val versionCode: String = infoDictionary?.get("CFBundleVersion") as? String ?: "0"
}
