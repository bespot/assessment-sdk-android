package com.bespot.sdk.mock.sample

import android.app.Application
import com.bespot.sdk.Bespot
import timber.log.Timber

class BespotSampleApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Init Timber for debug logs
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Init Bespot SDK with the provided App Id and App Secret
        Bespot.init(
            this,
            applicationId = "mock_sample_app",
            applicationSecret = "password",
            null
        )
    }
}
