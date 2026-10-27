package com.example

import com.example.data.CoupleRepository
import com.example.model.ActivityType
import com.example.model.LiveSessionMode
import com.example.model.PairingStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testRepositoryInitialState() {
        val repo = CoupleRepository()
        assertEquals(PairingStatus.CONNECTED, repo.pairingInfo.value.status)
        assertTrue(repo.pairingInfo.value.consentAccepted)
        assertEquals("dinda.safitri@gmail.com", repo.partnerState.value.email)
    }

    @Test
    fun testConnectPartner() {
        val repo = CoupleRepository()
        repo.connectPartner("ayu.lestari@gmail.com")
        assertEquals("ayu.lestari@gmail.com", repo.pairingInfo.value.partnerEmail)
        assertEquals(PairingStatus.CONNECTED, repo.pairingInfo.value.status)
        assertTrue(repo.pairingInfo.value.consentAccepted)
    }

    @Test
    fun testStatusAndActivityUpdate() {
        val repo = CoupleRepository()
        repo.updateMyStatus("Sedang di perpustakaan", ActivityType.AT_WORK)
        assertEquals("Sedang di perpustakaan", repo.currentUserState.value.customStatus)
        assertEquals(ActivityType.AT_WORK, repo.currentUserState.value.activityType)
    }

    @Test
    fun testGhostModeTogglesLocationSharing() {
        val repo = CoupleRepository()
        repo.toggleGhostMode(true)
        assertTrue(repo.currentUserState.value.isGhostMode)
        assertFalse(repo.currentUserState.value.isSharingLocation)

        repo.toggleGhostMode(false)
        assertFalse(repo.currentUserState.value.isGhostMode)
        assertTrue(repo.currentUserState.value.isSharingLocation)
    }

    @Test
    fun testShareConsensualSnapshot() {
        val repo = CoupleRepository()
        val initialCount = repo.activitySnapshots.value.size
        repo.shareConsensualSnapshot(
            appName = "Spotify",
            category = "Media & Musik",
            summary = "Mendengarkan playlist lofi",
            note = "Lagu kita diputar!"
        )
        assertEquals(initialCount + 1, repo.activitySnapshots.value.size)
        assertEquals("Spotify", repo.activitySnapshots.value.first().activeAppName)
    }

    @Test
    fun testLiveSessionCameraLifecycle() {
        val repo = CoupleRepository()
        assertFalse(repo.liveSessionState.value.isActive)

        repo.startLiveCameraSession()
        assertTrue(repo.liveSessionState.value.isActive)
        assertEquals(LiveSessionMode.CAMERA_2WAY, repo.liveSessionState.value.mode)

        repo.toggleMyCamera()
        assertFalse(repo.liveSessionState.value.isMyCameraOn)

        repo.toggleMic()
        assertTrue(repo.liveSessionState.value.isMicMuted)

        repo.flipCamera()
        assertFalse(repo.liveSessionState.value.isFrontCamera)

        repo.endLiveSession()
        assertFalse(repo.liveSessionState.value.isActive)
        assertEquals(LiveSessionMode.OFF, repo.liveSessionState.value.mode)
    }

    @Test
    fun testCaptureCameraPhoto() {
        val repo = CoupleRepository()
        val initialCount = repo.activitySnapshots.value.size
        repo.captureAndShareCameraPhoto(
            sceneDescription = "Selfie sedang di kafe",
            userNote = "Lagi nunggu pesanan"
        )
        assertEquals(initialCount + 1, repo.activitySnapshots.value.size)
        val snap = repo.activitySnapshots.value.first()
        assertTrue(snap.isCameraCapture)
        assertEquals("Selfie sedang di kafe", snap.activitySummary)
    }

    @Test
    fun testSendChatMessage() {
        val repo = CoupleRepository()
        val initialSize = repo.chatMessages.value.size
        repo.sendChatMessage("Halo sayang, jangan lupa makan ya!")
        assertEquals(initialSize + 1, repo.chatMessages.value.size)
        assertEquals("Halo sayang, jangan lupa makan ya!", repo.chatMessages.value.last().text)
    }

    @Test
    fun testSendChatSticker() {
        val repo = CoupleRepository()
        val initialSize = repo.chatMessages.value.size
        val sticker = repo.cuteStickers.first()
        repo.sendChatSticker(sticker)
        assertEquals(initialSize + 1, repo.chatMessages.value.size)
        val lastMsg = repo.chatMessages.value.last()
        assertEquals(sticker.name, lastMsg.sticker?.name)
        assertEquals(sticker.emoji, lastMsg.sticker?.emoji)
    }

    @Test
    fun testVoiceCallLifecycle() {
        val repo = CoupleRepository()
        assertEquals(com.example.model.CallState.IDLE, repo.activeCall.value.callState)

        repo.startCall(com.example.model.CallType.VOICE)
        assertEquals(com.example.model.CallState.OUTGOING_RINGING, repo.activeCall.value.callState)
        assertEquals(com.example.model.CallType.VOICE, repo.activeCall.value.callType)

        repo.answerCall()
        assertEquals(com.example.model.CallState.CONNECTED, repo.activeCall.value.callState)

        repo.tickCallDuration()
        repo.tickCallDuration()
        assertEquals(2, repo.activeCall.value.durationSeconds)

        repo.toggleCallMute()
        assertTrue(repo.activeCall.value.isMuted)

        repo.endCall()
        assertEquals(com.example.model.CallState.IDLE, repo.activeCall.value.callState)
    }

    @Test
    fun testVideoCallLifecycle() {
        val repo = CoupleRepository()
        repo.startCall(com.example.model.CallType.VIDEO)
        assertEquals(com.example.model.CallState.OUTGOING_RINGING, repo.activeCall.value.callState)
        assertEquals(com.example.model.CallType.VIDEO, repo.activeCall.value.callType)
        assertTrue(repo.activeCall.value.isVideoEnabled)

        repo.answerCall()
        assertEquals(com.example.model.CallState.CONNECTED, repo.activeCall.value.callState)

        repo.sendCallEmoji("❤️")
        assertTrue(repo.activeCall.value.floatingEmojis.contains("❤️"))

        repo.flipCallCamera()
        assertFalse(repo.activeCall.value.isFrontCamera)

        repo.endCall()
        assertEquals(com.example.model.CallState.IDLE, repo.activeCall.value.callState)
    }
}
