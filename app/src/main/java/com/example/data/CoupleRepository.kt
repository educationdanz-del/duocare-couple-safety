package com.example.data

import com.example.model.ActiveCallSession
import com.example.model.ActivityType
import com.example.model.CallState
import com.example.model.CallType
import com.example.model.ChatMessage
import com.example.model.ConsensualActivitySnapshot
import com.example.model.CuteSticker
import com.example.model.LiveSessionMode
import com.example.model.LiveSessionState
import com.example.model.LogEventType
import com.example.model.PairingInfo
import com.example.model.PairingStatus
import com.example.model.SafetyLogEvent
import com.example.model.UserPartnerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class CoupleRepository {

    private fun currentTime(): String =
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

    private val _currentUserState = MutableStateFlow(
        UserPartnerState(
            id = "user_me",
            name = "Rian Pratama",
            email = "freepalestineanime@gmail.com",
            isCurrentUser = true,
            batteryPercent = 88,
            isCharging = false,
            activityType = ActivityType.AT_WORK,
            speedKmh = 0f,
            customStatus = "Di kantor, fokus coding & meeting",
            latitude = -6.2088,
            longitude = 106.8456,
            locationName = "Sudirman Central Business District, Jakarta",
            lastUpdated = currentTime(),
            isSharingLocation = true,
            isSharingBattery = true,
            isSharingActivity = true,
            isGhostMode = false
        )
    )
    val currentUserState: StateFlow<UserPartnerState> = _currentUserState.asStateFlow()

    private val _partnerState = MutableStateFlow(
        UserPartnerState(
            id = "user_partner",
            name = "Dinda Safitri",
            email = "dinda.safitri@gmail.com",
            isCurrentUser = false,
            batteryPercent = 74,
            isCharging = true,
            activityType = ActivityType.WALKING,
            speedKmh = 4.2f,
            customStatus = "Sedang menuju kafe perpustakaan",
            latitude = -6.2255,
            longitude = 106.8025,
            locationName = "Senayan City Mall / Gelora, Jakarta",
            lastUpdated = currentTime(),
            isSharingLocation = true,
            isSharingBattery = true,
            isSharingActivity = true,
            isGhostMode = false
        )
    )
    val partnerState: StateFlow<UserPartnerState> = _partnerState.asStateFlow()

    private val _pairingInfo = MutableStateFlow(
        PairingInfo(
            partnerEmail = "dinda.safitri@gmail.com",
            partnerName = "Dinda Safitri",
            pairingCode = "DUO-7842",
            status = PairingStatus.CONNECTED,
            connectedAt = "Hari ini, 09:15 WIB",
            consentAccepted = true
        )
    )
    val pairingInfo: StateFlow<PairingInfo> = _pairingInfo.asStateFlow()

    private val _liveSessionState = MutableStateFlow(LiveSessionState())
    val liveSessionState: StateFlow<LiveSessionState> = _liveSessionState.asStateFlow()

    private val _activitySnapshots = MutableStateFlow(
        listOf(
            ConsensualActivitySnapshot(
                id = "snap_cam_1",
                senderName = "Dinda Safitri",
                timestamp = "5 menit yang lalu",
                activeAppName = "Kamera Terbuka Pasangan",
                appCategory = "Foto Kamera Langsung",
                activitySummary = "Foto selfie di meja perpustakaan",
                userNote = "Lagi baca modul bab 4, tempatnya sepi dan adem banget ❤️",
                cardColorHex = 0xFFE11D48,
                isCameraCapture = true,
                photoSceneDesc = "Dinda sedang duduk di meja perpustakaan dengan laptop dan segelas kopi dingin."
            ),
            ConsensualActivitySnapshot(
                id = "snap_1",
                senderName = "Dinda Safitri",
                timestamp = "15 menit yang lalu",
                activeAppName = "Google Maps",
                appCategory = "Navigasi & Perjalanan",
                activitySummary = "Membuka rute jalan kaki ke Kafe Senayan",
                userNote = "Jalan kaki sebentar ya sayang, udaranya lagi adem.",
                cardColorHex = 0xFF0D9488
            ),
            ConsensualActivitySnapshot(
                id = "snap_2",
                senderName = "Dinda Safitri",
                timestamp = "45 menit yang lalu",
                activeAppName = "Spotify",
                appCategory = "Musik & Podcast",
                activitySummary = "Mendengarkan playlist 'Acoustic Morning'",
                userNote = "Sambil baca catatan materi kuliah tadi pagi.",
                cardColorHex = 0xFF10B981
            ),
            ConsensualActivitySnapshot(
                id = "snap_3",
                senderName = "Rian Pratama",
                timestamp = "1 jam yang lalu",
                activeAppName = "Android Studio / Workspace",
                appCategory = "Pekerjaan & Produktivitas",
                activitySummary = "Menyelesaikan sprint project keselamatan",
                userNote = "Lagi review kode bareng tim, nanti siang makan bareng ya.",
                cardColorHex = 0xFF4F46E5
            )
        )
    )
    val activitySnapshots: StateFlow<List<ConsensualActivitySnapshot>> = _activitySnapshots.asStateFlow()

    private val _safetyLogs = MutableStateFlow(
        listOf(
            SafetyLogEvent(
                id = "log_1",
                timestamp = "5 menit yang lalu",
                title = "Foto Kamera Dibagikan 📸",
                description = "Dinda membagikan foto kamera langsung dengan persetujuan sukarela",
                type = LogEventType.CAMERA_PHOTO_CAPTURED
            ),
            SafetyLogEvent(
                id = "log_2",
                timestamp = "15 menit yang lalu",
                title = "Pembaruan Lokasi Dinda",
                description = "Dinda bergerak di sekitar Senayan City (Kecepatan: 4 km/jam)",
                type = LogEventType.LOCATION_UPDATE
            ),
            SafetyLogEvent(
                id = "log_3",
                timestamp = "35 menit yang lalu",
                title = "Check-In Tiba di Lokasi",
                description = "Dinda tiba di area Kampus / Senayan",
                type = LogEventType.ARRIVAL
            ),
            SafetyLogEvent(
                id = "log_4",
                timestamp = "1 jam yang lalu",
                title = "Status Pengisian Daya",
                description = "Ponsel Dinda mulai mengisi daya (Baterai 74%)",
                type = LogEventType.BATTERY_ALERT
            ),
            SafetyLogEvent(
                id = "log_5",
                timestamp = "09:15 WIB",
                title = "Koneksi Pasangan Aktif",
                description = "Kedua pihak telah menyetujui persetujuan berbagi lokasi, kamera, dan aktivitas",
                type = LogEventType.CONSENT_CHANGED
            )
        )
    )
    val safetyLogs: StateFlow<List<SafetyLogEvent>> = _safetyLogs.asStateFlow()

    val cuteStickers: List<CuteSticker> = listOf(
        CuteSticker("stk_1", "Peluk Manja", "🥰", "Cinta", "Kangeeen banget sayang!"),
        CuteSticker("stk_2", "Kecup Sayang", "😘", "Cinta", "Muach buat kamu!"),
        CuteSticker("stk_3", "Hati Berbunga", "💖", "Cinta", "Love you so much!"),
        CuteSticker("stk_4", "Kucing Manja", "😻", "Mimi", "Elus-elus kepalaku dong~"),
        CuteSticker("stk_5", "Kucing Ngambek", "😾", "Mimi", "Hmph! Balesnya lama nih."),
        CuteSticker("stk_6", "Kucing Bobo", "🐱💤", "Mimi", "Mau bobo dulu yaa."),
        CuteSticker("stk_7", "Ajak Makan", "🍜", "Kencan", "Makan bakso / mie bareng yuk!"),
        CuteSticker("stk_8", "Ngopi Santai", "☕", "Kencan", "Waktunya rehat ngopi berdua."),
        CuteSticker("stk_9", "Beli Es Krim", "🍦", "Kencan", "Nanti beliin es krim yaa!"),
        CuteSticker("stk_10", "Beruang Peluk", "🧸", "Peluk", "Peluk virtual hangat buat kamu."),
        CuteSticker("stk_11", "Muka Melas", "🥺", "Peluk", "Jangan lupa kabarin aku ya..."),
        CuteSticker("stk_12", "Hore Bahagia", "🥳", "Peluk", "Yeayyy seneng banget!")
    )

    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage(
                id = "msg_1",
                senderId = "user_me",
                senderName = "Rian Pratama",
                text = "Hai sayang, sudah sampai di Senayan belum?",
                timestamp = "09:20 WIB"
            ),
            ChatMessage(
                id = "msg_2",
                senderId = "user_partner",
                senderName = "Dinda Safitri",
                text = "Sudah sampai di kafe perpustakaan nih! Tempatnya nyaman banget.",
                timestamp = "09:22 WIB"
            ),
            ChatMessage(
                id = "msg_3",
                senderId = "user_partner",
                senderName = "Dinda Safitri",
                sticker = CuteSticker("stk_4", "Kucing Manja", "😻", "Mimi", "Elus-elus kepalaku dong~"),
                timestamp = "09:23 WIB"
            ),
            ChatMessage(
                id = "msg_4",
                senderId = "user_me",
                senderName = "Rian Pratama",
                sticker = CuteSticker("stk_1", "Peluk Manja", "🥰", "Cinta", "Kangeeen banget sayang!"),
                timestamp = "09:25 WIB"
            ),
            ChatMessage(
                id = "msg_5",
                senderId = "user_me",
                senderName = "Rian Pratama",
                text = "Nanti siang aku jemput ya, kita makan siang bareng ❤️",
                timestamp = "09:26 WIB"
            ),
            ChatMessage(
                id = "msg_6",
                senderId = "user_partner",
                senderName = "Dinda Safitri",
                sticker = CuteSticker("stk_7", "Ajak Makan", "🍜", "Kencan", "Makan bakso / mie bareng yuk!"),
                timestamp = "09:27 WIB"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val sender = if (_isViewingAsPartner.value) _partnerState.value else _currentUserState.value
        val msg = ChatMessage(
            id = UUID.randomUUID().toString(),
            senderId = sender.id,
            senderName = sender.name,
            text = text.trim(),
            timestamp = currentTime()
        )
        _chatMessages.value = _chatMessages.value + msg
    }

    fun sendChatSticker(sticker: CuteSticker) {
        val sender = if (_isViewingAsPartner.value) _partnerState.value else _currentUserState.value
        val msg = ChatMessage(
            id = UUID.randomUUID().toString(),
            senderId = sender.id,
            senderName = sender.name,
            sticker = sticker,
            timestamp = currentTime()
        )
        _chatMessages.value = _chatMessages.value + msg
    }

    private val _activeCall = MutableStateFlow(ActiveCallSession())
    val activeCall: StateFlow<ActiveCallSession> = _activeCall.asStateFlow()

    fun startCall(type: CallType) {
        val caller = if (_isViewingAsPartner.value) _partnerState.value else _currentUserState.value
        val receiver = if (_isViewingAsPartner.value) _currentUserState.value else _partnerState.value
        _activeCall.value = ActiveCallSession(
            callType = type,
            callState = CallState.OUTGOING_RINGING,
            callerName = caller.name,
            receiverName = receiver.name,
            durationSeconds = 0,
            isVideoEnabled = (type == CallType.VIDEO)
        )
    }

    fun answerCall() {
        _activeCall.value = _activeCall.value.copy(callState = CallState.CONNECTED)
    }

    fun endCall() {
        val call = _activeCall.value
        if (call.callState != CallState.IDLE) {
            val durationText = String.format(Locale.getDefault(), "%02d:%02d", call.durationSeconds / 60, call.durationSeconds % 60)
            val callName = if (call.callType == CallType.VIDEO) "Video Call" else "Panggilan Suara"
            addSafetyLog(
                title = "$callName Selesai",
                description = "Durasi panggilan bersama: $durationText",
                type = LogEventType.LIVE_SESSION_ENDED
            )
            val sender = if (_isViewingAsPartner.value) _partnerState.value else _currentUserState.value
            _chatMessages.value = _chatMessages.value + ChatMessage(
                id = UUID.randomUUID().toString(),
                senderId = sender.id,
                senderName = sender.name,
                text = "📞 $callName selesai • Durasi $durationText",
                timestamp = currentTime()
            )
        }
        _activeCall.value = ActiveCallSession(callState = CallState.IDLE)
    }

    fun tickCallDuration() {
        if (_activeCall.value.callState == CallState.CONNECTED) {
            _activeCall.value = _activeCall.value.copy(
                durationSeconds = _activeCall.value.durationSeconds + 1
            )
        }
    }

    fun toggleCallMute() {
        _activeCall.value = _activeCall.value.copy(isMuted = !_activeCall.value.isMuted)
    }

    fun toggleCallSpeaker() {
        _activeCall.value = _activeCall.value.copy(isSpeakerOn = !_activeCall.value.isSpeakerOn)
    }

    fun toggleCallVideo() {
        _activeCall.value = _activeCall.value.copy(isVideoEnabled = !_activeCall.value.isVideoEnabled)
    }

    fun flipCallCamera() {
        _activeCall.value = _activeCall.value.copy(isFrontCamera = !_activeCall.value.isFrontCamera)
    }

    fun sendCallEmoji(emoji: String) {
        val current = _activeCall.value.floatingEmojis
        _activeCall.value = _activeCall.value.copy(floatingEmojis = (current + emoji).takeLast(6))
    }

    // Active perspective in emulator (allows testing both perspectives)
    private val _isViewingAsPartner = MutableStateFlow(false)
    val isViewingAsPartner: StateFlow<Boolean> = _isViewingAsPartner.asStateFlow()

    fun togglePerspective() {
        _isViewingAsPartner.value = !_isViewingAsPartner.value
    }

    fun startLiveCameraSession() {
        val initiator = if (_isViewingAsPartner.value) _partnerState.value else _currentUserState.value
        _liveSessionState.value = LiveSessionState(
            isActive = true,
            mode = LiveSessionMode.CAMERA_2WAY,
            isMyCameraOn = true,
            isPartnerCameraOn = true,
            sessionStartedAt = currentTime()
        )
        addSafetyLog(
            title = "Sesi Kamera Terbuka Dimulai 📹",
            description = "${initiator.name} memulai sesi kamera tatap muka bersama dengan persetujuan terbuka.",
            type = LogEventType.LIVE_SESSION_STARTED
        )
    }

    fun startLiveScreenShareSession(screenTitle: String, category: String) {
        val initiator = if (_isViewingAsPartner.value) _partnerState.value else _currentUserState.value
        _liveSessionState.value = LiveSessionState(
            isActive = true,
            mode = LiveSessionMode.SCREEN_SHARE,
            isMyScreenSharing = true,
            isPartnerScreenSharing = true,
            activeScreenTitle = screenTitle,
            activeScreenCategory = category,
            sessionStartedAt = currentTime()
        )
        addSafetyLog(
            title = "Sesi Berbagi Layar Terbuka Dimulai 📱",
            description = "${initiator.name} memulai penyiaran layar bersama: $screenTitle",
            type = LogEventType.LIVE_SESSION_STARTED
        )
    }

    fun endLiveSession() {
        val closer = if (_isViewingAsPartner.value) _partnerState.value else _currentUserState.value
        val previousMode = _liveSessionState.value.mode
        _liveSessionState.value = LiveSessionState(isActive = false, mode = LiveSessionMode.OFF)
        addSafetyLog(
            title = "Sesi Terbuka Berakhir",
            description = "${closer.name} telah mengakhiri sesi ${if (previousMode == LiveSessionMode.CAMERA_2WAY) "kamera tatap muka" else "berbagi layar"}.",
            type = LogEventType.LIVE_SESSION_ENDED
        )
    }

    fun toggleMyCamera() {
        _liveSessionState.value = _liveSessionState.value.copy(
            isMyCameraOn = !_liveSessionState.value.isMyCameraOn
        )
    }

    fun togglePartnerCamera() {
        _liveSessionState.value = _liveSessionState.value.copy(
            isPartnerCameraOn = !_liveSessionState.value.isPartnerCameraOn
        )
    }

    fun toggleMic() {
        _liveSessionState.value = _liveSessionState.value.copy(
            isMicMuted = !_liveSessionState.value.isMicMuted
        )
    }

    fun flipCamera() {
        _liveSessionState.value = _liveSessionState.value.copy(
            isFrontCamera = !_liveSessionState.value.isFrontCamera
        )
    }

    fun switchSharedScreenContent(newTitle: String, newCategory: String) {
        _liveSessionState.value = _liveSessionState.value.copy(
            activeScreenTitle = newTitle,
            activeScreenCategory = newCategory
        )
        addSafetyLog(
            title = "Layar Ditukar: $newTitle",
            description = "Tampilan siaran layar terbuka diperbarui ke $newTitle",
            type = LogEventType.ACTIVITY_UPDATE
        )
    }

    fun captureAndShareCameraPhoto(sceneDescription: String, userNote: String) {
        val sender = if (_isViewingAsPartner.value) _partnerState.value else _currentUserState.value
        val newPhotoSnap = ConsensualActivitySnapshot(
            id = UUID.randomUUID().toString(),
            senderName = sender.name,
            timestamp = "Baru saja",
            activeAppName = "Kamera Terbuka Pasangan",
            appCategory = "Foto Kamera Langsung",
            activitySummary = sceneDescription,
            userNote = userNote,
            cardColorHex = 0xFFE11D48,
            isCameraCapture = true,
            photoSceneDesc = sceneDescription
        )
        _activitySnapshots.value = listOf(newPhotoSnap) + _activitySnapshots.value
        addSafetyLog(
            title = "Foto Kamera Baru Dibagikan 📸",
            description = "${sender.name} memotret dan membagikan foto kamera secara langsung: \"$sceneDescription\"",
            type = LogEventType.CAMERA_PHOTO_CAPTURED
        )
    }

    fun updateMyStatus(status: String, activity: ActivityType) {
        val targetFlow = if (_isViewingAsPartner.value) _partnerState else _currentUserState
        targetFlow.value = targetFlow.value.copy(
            customStatus = status,
            activityType = activity,
            lastUpdated = currentTime()
        )
        addSafetyLog(
            title = "Pembaruan Status",
            description = "${targetFlow.value.name}: \"$status\" (${activity.labelId})",
            type = LogEventType.ACTIVITY_UPDATE
        )
    }

    fun toggleLocationSharing(enabled: Boolean) {
        val targetFlow = if (_isViewingAsPartner.value) _partnerState else _currentUserState
        targetFlow.value = targetFlow.value.copy(isSharingLocation = enabled)
        addSafetyLog(
            title = if (enabled) "Berbagi Lokasi Diaktifkan" else "Berbagi Lokasi Dijeda",
            description = "${targetFlow.value.name} telah ${if (enabled) "mengaktifkan kembali" else "menjeda"} pembagian lokasi.",
            type = LogEventType.CONSENT_CHANGED
        )
    }

    fun toggleGhostMode(enabled: Boolean) {
        val targetFlow = if (_isViewingAsPartner.value) _partnerState else _currentUserState
        targetFlow.value = targetFlow.value.copy(
            isGhostMode = enabled,
            isSharingLocation = !enabled
        )
        addSafetyLog(
            title = if (enabled) "Mode Privasi Aktif" else "Mode Privasi Nonaktif",
            description = "${targetFlow.value.name} mengaktifkan mode privasi perlindungan sementara.",
            type = LogEventType.CONSENT_CHANGED
        )
    }

    fun toggleBatterySharing(enabled: Boolean) {
        val targetFlow = if (_isViewingAsPartner.value) _partnerState else _currentUserState
        targetFlow.value = targetFlow.value.copy(isSharingBattery = enabled)
    }

    fun sendCheckIn(location: String) {
        val sender = if (_isViewingAsPartner.value) _partnerState.value else _currentUserState.value
        addSafetyLog(
            title = "Check-In: Tiba dengan Selamat! ✅",
            description = "${sender.name} mengirim kabar telah sampai di $location pada ${currentTime()} WIB.",
            type = LogEventType.ARRIVAL
        )
    }

    fun triggerSos(message: String) {
        val senderFlow = if (_isViewingAsPartner.value) _partnerState else _currentUserState
        senderFlow.value = senderFlow.value.copy(
            isSosActive = true,
            sosMessage = message
        )
        addSafetyLog(
            title = "🚨 PERINGATAN DARURAT (SOS)",
            description = "Peringatan darurat dari ${senderFlow.value.name}: \"$message\" di ${senderFlow.value.locationName}",
            type = LogEventType.SOS_ALERT,
            isCritical = true
        )
    }

    fun dismissSos() {
        _currentUserState.value = _currentUserState.value.copy(isSosActive = false, sosMessage = null)
        _partnerState.value = _partnerState.value.copy(isSosActive = false, sosMessage = null)
        addSafetyLog(
            title = "Peringatan SOS Dihentikan",
            description = "Status darurat telah ditandai aman oleh pengguna.",
            type = LogEventType.SOS_ALERT
        )
    }

    fun shareConsensualSnapshot(
        appName: String,
        category: String,
        summary: String,
        note: String
    ) {
        val sender = if (_isViewingAsPartner.value) _partnerState.value else _currentUserState.value
        val color = when (category) {
            "Pekerjaan & Produktivitas" -> 0xFF4F46E5
            "Media & Musik" -> 0xFF10B981
            "Navigasi & Perjalanan" -> 0xFF0D9488
            "Komunikasi & Chat" -> 0xFFE11D48
            else -> 0xFF8B5CF6
        }
        val newSnap = ConsensualActivitySnapshot(
            id = UUID.randomUUID().toString(),
            senderName = sender.name,
            timestamp = "Baru saja",
            activeAppName = appName,
            appCategory = category,
            activitySummary = summary,
            userNote = note,
            cardColorHex = color
        )
        _activitySnapshots.value = listOf(newSnap) + _activitySnapshots.value
        addSafetyLog(
            title = "Snapshot Aktivitas Dibagikan 📱",
            description = "${sender.name} membagikan pratinjau aktivitas: $appName - \"$summary\"",
            type = LogEventType.SNAPSHOT_SHARED
        )
    }

    fun simulatePartnerMovement() {
        val partner = _partnerState.value
        val newLat = partner.latitude + (if (partner.latitude > -6.2150) -0.0035 else 0.0035)
        val newLng = partner.longitude + 0.0025
        val newSpeed = (15..45).random().toFloat()
        val locations = listOf(
            "Jl. Jenderal Sudirman No. 45, Jakarta",
            "Bundaran HI / Plaza Indonesia",
            "Kuningan City Mall, Jakarta Selatan",
            "Blok M Plaza, Kebayoran Baru",
            "Gelora Bung Karno, Senayan"
        )
        val newLocationName = locations.random()
        val newBattery = (partner.batteryPercent - 2).coerceAtLeast(15)

        _partnerState.value = partner.copy(
            latitude = newLat,
            longitude = newLng,
            speedKmh = newSpeed,
            activityType = if (newSpeed > 10) ActivityType.DRIVING else ActivityType.WALKING,
            locationName = newLocationName,
            batteryPercent = newBattery,
            lastUpdated = currentTime()
        )

        addSafetyLog(
            title = "Pergerakan Terdeteksi",
            description = "${partner.name} bergerak menuju $newLocationName (Kecepatan: ${newSpeed.toInt()} km/jam, Baterai: $newBattery%)",
            type = LogEventType.LOCATION_UPDATE
        )
    }

    fun connectPartner(email: String) {
        _pairingInfo.value = PairingInfo(
            partnerEmail = email,
            partnerName = if (email.contains("@")) email.substringBefore("@").replace(".", " ").capitalize(Locale.getDefault()) else "Pasangan",
            pairingCode = "DUO-${(1000..9999).random()}",
            status = PairingStatus.CONNECTED,
            connectedAt = "Hari ini, ${currentTime()} WIB",
            consentAccepted = true
        )
        _partnerState.value = _partnerState.value.copy(
            email = email,
            name = _pairingInfo.value.partnerName
        )
        addSafetyLog(
            title = "Koneksi Pasangan Berhasil",
            description = "Terhubung dengan $email melalui persetujuan bersama dua arah.",
            type = LogEventType.CONSENT_CHANGED
        )
    }

    fun disconnectPartner() {
        _pairingInfo.value = _pairingInfo.value.copy(
            status = PairingStatus.UNPAIRED,
            consentAccepted = false
        )
        addSafetyLog(
            title = "Koneksi Terputus",
            description = "Pembagian lokasi, kamera, dan status antar pasangan dinonaktifkan.",
            type = LogEventType.CONSENT_CHANGED
        )
    }

    private fun addSafetyLog(
        title: String,
        description: String,
        type: LogEventType,
        isCritical: Boolean = false
    ) {
        val event = SafetyLogEvent(
            id = UUID.randomUUID().toString(),
            timestamp = currentTime(),
            title = title,
            description = description,
            type = type,
            isCritical = isCritical
        )
        _safetyLogs.value = listOf(event) + _safetyLogs.value.take(40)
    }
}
