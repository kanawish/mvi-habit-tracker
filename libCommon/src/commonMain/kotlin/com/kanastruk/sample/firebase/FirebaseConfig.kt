package com.kanastruk.sample.firebase

// TODO: Use koin to build/inject instance.
class FirebaseConfig {
    companion object {
        const val Name = "libAndroid"

        // Temporary config constants until we have configuration files to read from.
        const val API_KEY = "AIzaSyC-vLYm2yYOFToLHo3El0c2Fv7CrXDf0-c"

        const val IDENTITY_TOOLKIT_ENDPOINT = "https://identitytoolkit.googleapis.com/"
        const val SECURE_TOKEN_ENDPOINT = "https://securetoken.googleapis.com/"

        const val RTDB_NAME = "mvihabittracker-default-rtdb"
        const val RTDB_URL = "https://$RTDB_NAME.firebaseio.com/"
    }

    enum class PrefKey {
        ID_TOKEN, LOCAL_ID, REFRESH_TOKEN
    }
}