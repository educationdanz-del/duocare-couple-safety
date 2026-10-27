package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.CallState
import com.example.ui.CoupleViewModel
import com.example.ui.components.CallScreenOverlay
import com.example.ui.components.CameraCaptureDialog
import com.example.ui.components.CheckInDialog
import com.example.ui.components.ShareSnapshotDialog
import com.example.ui.components.SosAlertDialog
import com.example.ui.screens.ActivityScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MapScreen
import com.example.ui.screens.PairingScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CoupleApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoupleApp(viewModel: CoupleViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showSos by viewModel.showSosDialog.collectAsStateWithLifecycle()
    val showCheckIn by viewModel.showCheckInDialog.collectAsStateWithLifecycle()
    val showSnapshot by viewModel.showSnapshotDialog.collectAsStateWithLifecycle()
    val showCameraCapture by viewModel.showCameraCaptureDialog.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) }

    // Custom back handler: return to Home (tab 0) if on other screens
    if (selectedTab != 0) {
        BackHandler {
            selectedTab = 0
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE11D48))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "DuoCare",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFDCFCE7)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF16A34A))
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Saling Terbuka",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF15803D)
                                    )
                                }
                            }
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { viewModel.togglePerspective() },
                            modifier = Modifier.testTag("top_bar_switch_perspective_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Tukar Sudut Pandang",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    // Tab 0: Beranda
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == 0) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Beranda"
                            )
                        },
                        label = { Text("Beranda", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_home_button")
                    )

                    // Tab 1: Radar Peta
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == 1) Icons.Default.Map else Icons.Outlined.Map,
                                contentDescription = "Radar"
                            )
                        },
                        label = { Text("Radar", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_map_button")
                    )

                    // Tab 2: Obrolan & Stiker
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = {
                            BadgedBox(
                                badge = {
                                    Badge(containerColor = Color(0xFF10B981)) {
                                        Text(uiState.chatMessages.size.toString())
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (selectedTab == 2) Icons.AutoMirrored.Filled.Chat else Icons.AutoMirrored.Outlined.Chat,
                                    contentDescription = "Obrolan"
                                )
                            }
                        },
                        label = { Text("Obrolan", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_chat_button")
                    )

                    // Tab 3: Foto & Layar
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == 3) Icons.Default.PhotoCamera else Icons.Outlined.PhotoCamera,
                                contentDescription = "Layar"
                            )
                        },
                        label = { Text("Layar", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_activity_button")
                    )

                    // Tab 4: Tautan & Izin
                    NavigationBarItem(
                        selected = selectedTab == 4,
                        onClick = { selectedTab = 4 },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == 4) Icons.Default.Lock else Icons.Outlined.Lock,
                                contentDescription = "Tautan"
                            )
                        },
                        label = { Text("Tautan", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_pairing_button")
                    )
                }
            }
        ) { innerPadding ->
            Crossfade(
                targetState = selectedTab,
                label = "ScreenTransition",
                modifier = Modifier.padding(innerPadding)
            ) { tabIndex ->
                when (tabIndex) {
                    0 -> HomeScreen(
                        uiState = uiState,
                        onTogglePerspective = { viewModel.togglePerspective() },
                        onSimulateMovement = { viewModel.simulateMovement() },
                        onOpenSosDialog = { viewModel.openSosDialog(true) },
                        onOpenCheckInDialog = { viewModel.openCheckInDialog(true) },
                        onOpenSnapshotDialog = { viewModel.openSnapshotDialog(true) },
                        onOpenPhotoCaptureDialog = { viewModel.openCameraCaptureDialog(true) },
                        onStartCameraSession = { viewModel.startLiveCameraSession() },
                        onStartScreenShareSession = {
                            viewModel.startLiveScreenShareSession(
                                "Google Maps - Rute Navigasi Bersama",
                                "Navigasi & Perjalanan"
                            )
                        },
                        onEndLiveSession = { viewModel.endLiveSession() },
                        onToggleMyCamera = { viewModel.toggleMyCamera() },
                        onToggleMic = { viewModel.toggleMic() },
                        onFlipCamera = { viewModel.flipCamera() },
                        onSwitchScreenContent = { title, cat -> viewModel.switchSharedScreenContent(title, cat) },
                        onStartVoiceCall = { viewModel.startVoiceCall() },
                        onStartVideoCall = { viewModel.startVideoCall() },
                        onNavigateToMap = { selectedTab = 1 },
                        onNavigateToActivities = { selectedTab = 3 },
                        onDismissSos = { viewModel.dismissSos() }
                    )
                    1 -> MapScreen(
                        uiState = uiState,
                        onSimulateMovement = { viewModel.simulateMovement() },
                        onToggleLocationSharing = { viewModel.toggleLocationSharing(it) },
                        onToggleGhostMode = { viewModel.toggleGhostMode(it) }
                    )
                    2 -> ChatScreen(
                        uiState = uiState,
                        cuteStickers = viewModel.cuteStickers,
                        onSendMessage = { viewModel.sendChatMessage(it) },
                        onSendSticker = { viewModel.sendChatSticker(it) },
                        onStartVoiceCall = { viewModel.startVoiceCall() },
                        onStartVideoCall = { viewModel.startVideoCall() }
                    )
                    3 -> ActivityScreen(
                        uiState = uiState,
                        onOpenSnapshotDialog = { viewModel.openSnapshotDialog(true) },
                        onOpenPhotoCaptureDialog = { viewModel.openCameraCaptureDialog(true) }
                    )
                    4 -> PairingScreen(
                        uiState = uiState,
                        onConnectPartner = { viewModel.connectPartner(it) },
                        onDisconnectPartner = { viewModel.disconnectPartner() },
                        onUpdateMyStatus = { status, act -> viewModel.updateMyStatus(status, act) },
                        onToggleLocationSharing = { viewModel.toggleLocationSharing(it) },
                        onToggleBatterySharing = { viewModel.toggleBatterySharing(it) },
                        onToggleGhostMode = { viewModel.toggleGhostMode(it) }
                    )
                }
            }
        }

        // Full Screen Real-time Call Overlay (Voice & Video Call)
        if (uiState.activeCall.callState != CallState.IDLE) {
            CallScreenOverlay(
                callSession = uiState.activeCall,
                myProfile = uiState.myProfile,
                partnerProfile = uiState.partnerProfile,
                onAnswerCall = { viewModel.answerCall() },
                onEndCall = { viewModel.endCall() },
                onToggleMute = { viewModel.toggleCallMute() },
                onToggleSpeaker = { viewModel.toggleCallSpeaker() },
                onToggleVideo = { viewModel.toggleCallVideo() },
                onFlipCamera = { viewModel.flipCallCamera() },
                onSendEmoji = { emoji -> viewModel.sendCallEmoji(emoji) }
            )
        }
    }

    // Dialogs
    if (showSos) {
        SosAlertDialog(
            onDismiss = { viewModel.openSosDialog(false) },
            onConfirmSos = { message -> viewModel.triggerSos(message) }
        )
    }

    if (showCheckIn) {
        CheckInDialog(
            onDismiss = { viewModel.openCheckInDialog(false) },
            onConfirmCheckIn = { place -> viewModel.sendCheckIn(place) }
        )
    }

    if (showSnapshot) {
        ShareSnapshotDialog(
            onDismiss = { viewModel.openSnapshotDialog(false) },
            onConfirmShare = { app, cat, sum, note ->
                viewModel.shareActivitySnapshot(app, cat, sum, note)
            }
        )
    }

    if (showCameraCapture) {
        CameraCaptureDialog(
            onDismiss = { viewModel.openCameraCaptureDialog(false) },
            onConfirmCapture = { scene, note ->
                viewModel.captureAndShareCameraPhoto(scene, note)
            }
        )
    }
}
