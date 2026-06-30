package com.momosi.trucktrack.user.internal

import android.content.Intent
import androidx.activity.ComponentActivity
import org.koin.android.ext.android.inject

class AuthActivity : ComponentActivity() {

    private val openIdManager: OpenIdManager by inject()

    override fun onResume() {
        super.onResume()

        openIdManager.onAuthIntentReceived(intent)

        finish()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        openIdManager.onAuthIntentReceived(intent)

        finish()
    }
}
