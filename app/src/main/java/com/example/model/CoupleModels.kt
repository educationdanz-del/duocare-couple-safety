package com.example.model

enum class ActivityType(val labelId: String, val description: String) {
    STATIONARY("Berdiam", "Sedang tidak bergerak / beristirahat"),
    WALKING("Berjalan", "Sedang berjalan santai"),
    DRIVING("Berkendara", "Bergerak dengan kendaraan"),
    AT_HOME("Di Rumah", "Di zona aman rumah"),
    AT_WORK("Di Kantor/Kampus", "Sedang beraktivitas kerja"),
    OUTDOORS("Di Luar", "Sedang berada di ruang publik")
}

enum class PairingStatus {
    UNPAIRED,
    REQUEST_SENT,
    CONNECTED
}

enum class LogEventType {
    LOCATION_UPDATE,
    ARRIVAL,
    BATTERY_ALERT,
    ACTIVITY_UPDATE,
    SNAPSHOT_SHARED,
    LIVE_SESSION_STARTED,
    LIVE_SESSION_ENDED,
    CAMERA_PHOTO_CAPTURED,
    SOS_ALERT,
    CONSENT_CHANGED
}

data class SafetyLogEvent(
    val id: String,
    val timestamp: String,
    val title: String,
    val description: String,
    val type: LogEventType,
    val isCritical: Boolean = false
)

data class ConsensualActivitySnapshot(
    val id: String,
    val senderName: String,
    val timestamp: String,
    val activeAppName: String,
    val appCategory: String,
    val activitySummary: String,
    val userNote: String,
    val cardColorHex: Long = 0xFF4F46E5,
    val isCameraCapture: Boolean = false,
    val photoSceneDesc: String? = null
)

enum class LiveSessionMode {
    OFF,
    CAMERA_2WAY,
    SCREEN_SHARE
}

data class LiveSessionState(
    val isActive: Boolean = false,
    val mode: LiveSessionMode = LiveSessionMode.OFF,
    val isMyCameraOn: Boolean = true,
    val isPartnerCameraOn: Boolean = true,
    val isMyScreenSharing: Boolean = false,
    val isPartnerScreenSharing: Boolean = false,
    val activeScreenTitle: String = "Google Maps - Rute Senayan",
    val activeScreenCategory: String = "Navigasi & Perjalanan",
    val sessionStartedAt: String = "00:00",
    val isMicMuted: Boolean = false,
    val isFrontCamera: Boolean = true
)

data class UserPartnerState(
    val id: String,
    val name: String,
    val email: String,
    val isCurrentUser: Boolean,
    val batteryPercent: Int,
    val isCharging: Boolean,
    val activityType: ActivityType,
    val speedKmh: Float,
    val customStatus: String,
    val latitude: Double,
    val longitude: Double,
    val locationName: String,
    val lastUpdated: String,
    val isSharingLocation: Boolean = true,
    val isSharingBattery: Boolean = true,
    val isSharingActivity: Boolean = true,
    val isGhostMode: Boolean = false,
    val isSosActive: Boolean = false,
    val sosMessage: String? = null
)

data class PairingInfo(
    val partnerEmail: String,
    val partnerName: String,
    val pairingCode: String,
    val status: PairingStatus,
    val connectedAt: String,
    val consentAccepted: Boolean
)

data class CuteSticker(
    val id: String,
    val name: String,
    val emoji: String,
    val category: String,
    val caption: String
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val senderName: String,
    val text: String = "",
    val sticker: CuteSticker? = null,
    val timestamp: String,
    val isRead: Boolean = true
)

enum class CallType {
    NONE,
    VOICE,
    VIDEO
}

enum class CallState {
    IDLE,
    OUTGOING_RINGING,
    INCOMING_RINGING,
    CONNECTED,
    ENDED
}

data class ActiveCallSession(
    val callType: CallType = CallType.NONE,
    val callState: CallState = CallState.IDLE,
    val callerName: String = "",
    val receiverName: String = "",
    val durationSeconds: Int = 0,
    val isMuted: Boolean = false,
    val isSpeakerOn: Boolean = true,
    val isVideoEnabled: Boolean = true,
    val isFrontCamera: Boolean = true,
    val floatingEmojis: List<String> = emptyList()
)
