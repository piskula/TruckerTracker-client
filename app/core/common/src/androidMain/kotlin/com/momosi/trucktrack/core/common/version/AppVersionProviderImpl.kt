package com.momosi.trucktrack.core.common.version

import android.content.Context

class AppVersionProviderImpl(context: Context) : AppVersionProvider {

    private val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)

    override val versionName: String = packageInfo.versionName ?: "dev"

    override val versionCode: String = packageInfo.longVersionCode.toString()
}
