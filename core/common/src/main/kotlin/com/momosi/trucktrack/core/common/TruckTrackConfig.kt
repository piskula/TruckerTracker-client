package com.momosi.trucktrack.core.common

object TruckTrackConfig {
    const val API_BASE_URL = "https://tt.momosi.org/"
    const val REALM_URL = "https://sso.momosi.org/realms/trucktrack/"

    const val OAUTH_CLIENT_ID = "trucktrack-app"
    const val APP_SCHEME = "com.momosi.trucktrack"
    const val OAUTH_REDIRECT_URL = "$APP_SCHEME://auth/callback"
    const val OAUTH_LOGOUT_REDIRECT_URL = "$APP_SCHEME://auth/logout"
}

