package com.momosi.trucktrack.user.internal

import android.content.Intent
import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {

    @Inject
    lateinit var openIdManager: OpenIdManager

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
