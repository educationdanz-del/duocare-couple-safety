package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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

@Composable
fun SosAlertDialog(
    onDismiss: () -> Unit,
    onConfirmSos: (String) -> Unit
) {
    var selectedReason by remember { mutableStateOf("Membutuhkan bantuan segera / dalam situasi darurat!") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFE11D48),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Picu Peringatan SOS?",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE11D48)
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "Notifikasi darurat dan koordinat lokasi presisi Anda akan segera dikirimkan ke ponsel pasangan dengan prioritas tertinggi.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Pesan Darurat:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                listOf(
                    "Membutuhkan bantuan segera / situasi darurat!",
                    "Terjadi kendala kendaraan di jalan, mohon hubungi saya.",
                    "Merasa tidak aman di sekitar lokasi ini."
                ).forEach { reason ->
                    Surface(
                        onClick = { selectedReason = reason },
                        shape = RoundedCornerShape(10.dp),
                        color = if (selectedReason == reason) Color(0xFFFFE4E6) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = BorderStroke(
                            1.dp,
                            if (selectedReason == reason) Color(0xFFE11D48) else Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = reason,
                            fontSize = 12.sp,
                            color = if (selectedReason == reason) Color(0xFF9F1239) else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirmSos(selectedReason) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("confirm_sos_button")
            ) {
                Text("Kirim SOS Sekarang", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Composable
fun CheckInDialog(
    onDismiss: () -> Unit,
    onConfirmCheckIn: (String) -> Unit
) {
    var locationInput by remember { mutableStateOf("Kantor / Tempat Kerja") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Kirim Check-In Tiba",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "Beri kabar cepat ke pasangan bahwa Anda telah sampai di tempat tujuan dengan selamat.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(14.dp))

                listOf(
                    "Rumah Kita",
                    "Kantor / Tempat Kerja",
                    "Kampus / Universitas",
                    "Kafe / Tempat Makan"
                ).forEach { place ->
                    Surface(
                        onClick = { locationInput = place },
                        shape = RoundedCornerShape(10.dp),
                        color = if (locationInput == place) Color(0xFFDCFCE7) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = BorderStroke(
                            1.dp,
                            if (locationInput == place) Color(0xFF16A34A) else Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = "📍 $place",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (locationInput == place) Color(0xFF15803D) else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirmCheckIn(locationInput) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("confirm_checkin_button")
            ) {
                Text("Kirim Kabar", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Composable
fun ShareSnapshotDialog(
    onDismiss: () -> Unit,
    onConfirmShare: (String, String, String, String) -> Unit
) {
    val presets = listOf(
        Pair("Google Maps", "Navigasi & Perjalanan"),
        Pair("Spotify", "Media & Musik"),
        Pair("Android Studio / Dokumen", "Pekerjaan & Produktivitas"),
        Pair("WhatsApp / Pesan", "Komunikasi & Chat"),
        Pair("Shopee / Belanja", "Gaya Hidup & Belanja")
    )

    var selectedApp by remember { mutableStateOf(presets[0]) }
    var summaryInput by remember { mutableStateOf("Sedang membuka rute jalan ke Senayan") }
    var noteInput by remember { mutableStateOf("Bentar lagi sampai ya sayang!") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    tint = Color(0xFF4F46E5),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Bagikan Snapshot Aktivitas",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "Kirim pratinjau aktivitas aplikasi dan catatan terkini ke pasangan dengan persetujuan sukarela.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Pilih Aplikasi / Aktivitas:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    presets.take(3).forEach { (app, cat) ->
                        Surface(
                            onClick = {
                                selectedApp = Pair(app, cat)
                                summaryInput = when (app) {
                                    "Google Maps" -> "Sedang memeriksa estimasi waktu perjalanan"
                                    "Spotify" -> "Mendengarkan musik playlist favorit"
                                    else -> "Sedang menyelesaikan pekerjaan di layar"
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            color = if (selectedApp.first == app) Color(0xFFEEF2FF) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = BorderStroke(
                                1.dp,
                                if (selectedApp.first == app) Color(0xFF4F46E5) else Color.Transparent
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = app.substringBefore(" /"),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (selectedApp.first == app) Color(0xFF312E81) else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = summaryInput,
                    onValueChange = { summaryInput = it },
                    label = { Text("Ringkasan Aktivitas Layar") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = noteInput,
                    onValueChange = { noteInput = it },
                    label = { Text("Catatan Cinta / Pesan ke Pasangan") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmShare(
                        selectedApp.first,
                        selectedApp.second,
                        summaryInput,
                        noteInput
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("confirm_share_snapshot_button")
            ) {
                Text("Kirim Snapshot", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Composable
fun CameraCaptureDialog(
    onDismiss: () -> Unit,
    onConfirmCapture: (String, String) -> Unit
) {
    val scenePresets = listOf(
        "Selfie sedang istirahat di meja",
        "Foto pemandangan sekitar",
        "Foto makanan / minuman saat ini",
        "Sedang berada di jalan / transportasi"
    )

    var selectedScene by remember { mutableStateOf(scenePresets[0]) }
    var noteInput by remember { mutableStateOf("Kabar langsung dari kamera aku ❤️") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = null,
                    tint = Color(0xFFE11D48),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Potret & Bagikan Foto Kamera",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "Ambil foto suasana terkini dan bagikan langsung ke feed pasangan dengan persetujuan sukarela.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Pilih Situasi / Objek:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                scenePresets.forEach { scene ->
                    Surface(
                        onClick = { selectedScene = scene },
                        shape = RoundedCornerShape(10.dp),
                        color = if (selectedScene == scene) Color(0xFFFFE4E6) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = BorderStroke(
                            1.dp,
                            if (selectedScene == scene) Color(0xFFE11D48) else Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                    ) {
                        Text(
                            text = "📸 $scene",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (selectedScene == scene) Color(0xFF9F1239) else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = noteInput,
                    onValueChange = { noteInput = it },
                    label = { Text("Pesan untuk Pasangan") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirmCapture(selectedScene, noteInput) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("confirm_capture_photo_button")
            ) {
                Text("Kirim Foto Kamera", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

