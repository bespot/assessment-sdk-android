package com.bespot.sdk.mock.sample

import com.bespot.sdk.Status
import com.bespot.sdk.StatusResult
import com.bespot.sdk.common.Failure
import com.bespot.sdk.common.StatusFailure
import java.util.*

enum class InOutStatus {
    INSIDE, OUTSIDE, AWAY, UNVERIFIED, ERROR
}

data class StatusWrapper(
    val status: InOutStatus,
    val description: String,
    val timestamp: Long
) {
    companion object {
        fun success(result: StatusResult): StatusWrapper = StatusWrapper(
            status = when (result.status) {
                Status.IN -> InOutStatus.INSIDE
                Status.OUT -> InOutStatus.OUTSIDE
                Status.AWAY -> InOutStatus.AWAY
                else -> InOutStatus.UNVERIFIED
            },
            description = "Resolved EIDs: ${result.eids.size}",
            timestamp = result.timestamp
        )

        fun error(error: Failure): StatusWrapper = StatusWrapper(
            status = InOutStatus.ERROR,
            description = error.toText(),
            timestamp = Date().time
        )
    }
}

fun Failure.toText(): String {
    return when (this) {
        is StatusFailure.NoConfigurationFound -> "No Configuration Found"
        is StatusFailure.NoStoreReadings -> "No Store Readings"
        is StatusFailure.CloseDistance -> "Close Distance"
        is StatusFailure.NoStatusCached -> "No cached status"
        is StatusFailure.IndoorDataModelNotFound -> "Indoor data model Not found"
        is Failure.BluetoothPermissionDenied -> "Bluetooth Permission Denied"
        is Failure.BluetoothDisabled -> "Bluetooth Disabled"
        is Failure.LocationPermissionDenied -> "Location Permission Denied"
        is Failure.NetworkConnection -> "Connection error"
        is Failure.NotInitialized -> "SDK isn't initialized"
        is Failure.ServerError -> "Remote server error"
        is Failure.FeaturePermissionDenied -> "Feature is not available"
        else -> "Unmapped error: $this"
    }
}
