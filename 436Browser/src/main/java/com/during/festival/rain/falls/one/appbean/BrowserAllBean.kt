package com.during.festival.rain.falls.one.appbean

import androidx.annotation.Keep

@Keep
data class BrowserAllBean(
    val applicationSettings: ApplicationSettings,
    val assetManagement: AssetManagement,
    val networkConfiguration: NetworkConfiguration
)
@Keep
data class ApplicationSettings(
    val exposureControls: ExposureControls,
    val schedule: Schedule,
    val userProfile: UserProfile
)

@Keep
data class AssetManagement(
    val delayConfiguration: DelayConfiguration,
    val identifiers: Identifiers
)

@Keep
data class NetworkConfiguration(
    val webSettings: WebSettings
)

@Keep
data class ExposureControls(
    val displayLimits: DisplayLimits,
    val interactionLimits: Int
)

@Keep
data class Schedule(
    val checkIntervals: List<Int>,
    val maxFailures: Int
)

@Keep
data class UserProfile(
    val syncEnabled: Boolean,
    val tier: Int
)

@Keep
data class DisplayLimits(
    val daily: Int,
    val hourly: Int
)

@Keep
data class DelayConfiguration(
    val maxDelay: Int,
    val minDelay: Int
)

@Keep
data class Identifiers(
    val internalId: String,
    val socialId: String
)

@Keep
data class WebSettings(
    val endpoints: List<Endpoint>,
    val initialDelay: Int,
    val packageName: String
)

@Keep
data class Endpoint(
    val limits: Limits,
    val url: String
)

@Keep
data class Limits(
    val daily: Int,
    val hourly: Int
)


