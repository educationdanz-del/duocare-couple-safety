package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CoupleRepository
import com.example.model.ActiveCallSession
import com.example.model.ActivityType
import com.example.model.CallType
import com.example.model.ChatMessage
import com.example.model.ConsensualActivitySnapshot
import com.example.model.CuteSticker
import com.example.model.LiveSessionState
import com.example.model.PairingInfo
import com.example.model.SafetyLogEvent
import com.example.model.UserPartnerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class CoupleUiState(
    val myProfile: UserPartnerState,
    val partnerProfile: UserPartnerState,
    val pairingInfo: PairingInfo,
    val distanceKm: Double,
    val isViewingAsPartner: Boolean,
    val snapshots: List<ConsensualActivitySnapshot>,
    val logs: List<SafetyLogEvent>,
    val liveSession: LiveSessionState,
    val chatMessages: List<ChatMessage>,
    val activeCall: ActiveCallSession
)

class CoupleViewModel(
    private val repository: CoupleRepository = CoupleRepository()
) : ViewModel() {

    val isViewingAsPartner: StateFlow<Boolean> = repository.isViewingAsPartner
    val pairingInfo: StateFlow<PairingInfo> = repository.pairingInfo
    val snapshots: StateFlow<List<ConsensualActivitySnapshot>> = repository.activitySnapshots
    val logs: StateFlow<List<SafetyLogEvent>> = repository.safetyLogs
    val liveSession: StateFlow<LiveSessionState> = repository.liveSessionState
    val cuteStickers: List<CuteSticker> = repository.cuteStickers
    val activeCall: StateFlow<ActiveCallSession> = repository.activeCall

    // Dialog & UI temporary states
    private val _showSosDialog = MutableStateFlow(false)
    val showSosDialog: StateFlow<Boolean> = _showSosDialog.asStateFlow()

    private val _showCheckInDialog = MutableStateFlow(false)
    val showCheckInDialog: StateFlow<Boolean> = _showCheckInDialog.asStateFlow()

    private val _showSnapshotDialog = MutableStateFlow(false)
    val showSnapshotDialog: StateFlow<Boolean> = _showSnapshotDialog.asStateFlow()

    private val _showCameraCaptureDialog = MutableStateFlow(false)
    val showCameraCaptureDialog: StateFlow<Boolean> = _showCameraCaptureDialog.asStateFlow()

    private val _showLiveSessionDialog = MutableStateFlow(false)
    val showLiveSessionDialog: StateFlow<Boolean> = _showLiveSessionDialog.asStateFlow()

    private val _showPairDialog = MutableStateFlow(false)
    val showPairDialog: StateFlow<Boolean> = _showPairDialog.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                repository.tickCallDuration()
            }
        }
    }

    val uiState: StateFlow<CoupleUiState> = combine(
        combine(
            repository.currentUserState,
            repository.partnerState,
            repository.pairingInfo
        ) { me, partner, pairing ->
            Triple(me, partner, pairing)
        },
        combine(
            repository.isViewingAsPartner,
            repository.activitySnapshots,
            repository.safetyLogs
        ) { viewing, snaps, logsList ->
            Triple(viewing, snaps, logsList)
        },
        combine(
            repository.liveSessionState,
            repository.chatMessages,
            repository.activeCall
        ) { live, msgs, call ->
            Triple(live, msgs, call)
        }
    ) { group1, group2, group3 ->
        val (me, partner, pairing) = group1
        val (viewingPartner, snaps, logsList) = group2
        val (live, msgs, call) = group3
        val activeMe = if (viewingPartner) partner else me
        val activePartner = if (viewingPartner) me else partner
        val distance = calculateDistance(
            activeMe.latitude, activeMe.longitude,
            activePartner.latitude, activePartner.longitude
        )
        CoupleUiState(
            myProfile = activeMe,
            partnerProfile = activePartner,
            pairingInfo = pairing,
            distanceKm = distance,
            isViewingAsPartner = viewingPartner,
            snapshots = snaps,
            logs = logsList,
            liveSession = live,
            chatMessages = msgs,
            activeCall = call
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        CoupleUiState(
            myProfile = repository.currentUserState.value,
            partnerProfile = repository.partnerState.value,
            pairingInfo = repository.pairingInfo.value,
            distanceKm = 4.8,
            isViewingAsPartner = false,
            snapshots = emptyList(),
            logs = emptyList(),
            liveSession = LiveSessionState(),
            chatMessages = emptyList(),
            activeCall = ActiveCallSession()
        )
    )

    fun togglePerspective() = repository.togglePerspective()

    fun updateMyStatus(status: String, activity: ActivityType) =
        repository.updateMyStatus(status, activity)

    fun toggleLocationSharing(enabled: Boolean) = repository.toggleLocationSharing(enabled)

    fun toggleGhostMode(enabled: Boolean) = repository.toggleGhostMode(enabled)

    fun toggleBatterySharing(enabled: Boolean) = repository.toggleBatterySharing(enabled)

    fun sendChatMessage(text: String) = repository.sendChatMessage(text)

    fun sendChatSticker(sticker: CuteSticker) = repository.sendChatSticker(sticker)

    // Call functions
    fun startVoiceCall() = repository.startCall(CallType.VOICE)
    fun startVideoCall() = repository.startCall(CallType.VIDEO)
    fun answerCall() = repository.answerCall()
    fun endCall() = repository.endCall()
    fun toggleCallMute() = repository.toggleCallMute()
    fun toggleCallSpeaker() = repository.toggleCallSpeaker()
    fun toggleCallVideo() = repository.toggleCallVideo()
    fun flipCallCamera() = repository.flipCallCamera()
    fun sendCallEmoji(emoji: String) = repository.sendCallEmoji(emoji)

    fun sendCheckIn(location: String) {
        repository.sendCheckIn(location)
        _showCheckInDialog.value = false
    }

    fun triggerSos(message: String) {
        repository.triggerSos(message)
        _showSosDialog.value = false
    }

    fun dismissSos() = repository.dismissSos()

    fun shareActivitySnapshot(appName: String, category: String, summary: String, note: String) {
        repository.shareConsensualSnapshot(appName, category, summary, note)
        _showSnapshotDialog.value = false
    }

    fun captureAndShareCameraPhoto(sceneDescription: String, userNote: String) {
        repository.captureAndShareCameraPhoto(sceneDescription, userNote)
        _showCameraCaptureDialog.value = false
    }

    fun startLiveCameraSession() {
        repository.startLiveCameraSession()
        _showLiveSessionDialog.value = false
    }

    fun startLiveScreenShareSession(screenTitle: String, category: String) {
        repository.startLiveScreenShareSession(screenTitle, category)
        _showLiveSessionDialog.value = false
    }

    fun endLiveSession() = repository.endLiveSession()
    fun toggleMyCamera() = repository.toggleMyCamera()
    fun togglePartnerCamera() = repository.togglePartnerCamera()
    fun toggleMic() = repository.toggleMic()
    fun flipCamera() = repository.flipCamera()
    fun switchSharedScreenContent(newTitle: String, newCategory: String) =
        repository.switchSharedScreenContent(newTitle, newCategory)

    fun simulateMovement() = repository.simulatePartnerMovement()

    fun connectPartner(email: String) {
        repository.connectPartner(email)
        _showPairDialog.value = false
    }

    fun disconnectPartner() = repository.disconnectPartner()

    fun openSosDialog(open: Boolean) { _showSosDialog.value = open }
    fun openCheckInDialog(open: Boolean) { _showCheckInDialog.value = open }
    fun openSnapshotDialog(open: Boolean) { _showSnapshotDialog.value = open }
    fun openCameraCaptureDialog(open: Boolean) { _showCameraCaptureDialog.value = open }
    fun openLiveSessionDialog(open: Boolean) { _showLiveSessionDialog.value = open }
    fun openPairDialog(open: Boolean) { _showPairDialog.value = open }

    private fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val r = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return String.format(java.util.Locale.US, "%.1f", r * c).toDoubleOrNull() ?: 4.5
    }
}
