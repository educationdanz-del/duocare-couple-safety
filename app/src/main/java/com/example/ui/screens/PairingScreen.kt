package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import com.example.model.ActivityType
import com.example.model.PairingStatus
import com.example.ui.CoupleUiState

@Composable
fun PairingScreen(
    uiState: CoupleUiState,
    onConnectPartner: (String) -> Unit,
    onDisconnectPartner: () -> Unit,
    onUpdateMyStatus: (String, ActivityType) -> Unit,
    onToggleLocationSharing: (Boolean) -> Unit,
    onToggleBatterySharing: (Boolean) -> Unit,
    onToggleGhostMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val me = uiState.myProfile
    val pairing = uiState.pairingInfo

    var partnerEmailInput by remember { mutableStateOf("") }
    var myStatusInput by remember(me.customStatus) { mutableStateOf(me.customStatus) }
    var selectedActivity by remember(me.activityType) { mutableStateOf(me.activityType) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Current Pairing Status Banner
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (pairing.status == PairingStatus.CONNECTED) Color(0xFFF0FDF4) else Color(0xFFFFF1F2)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (pairing.status == PairingStatus.CONNECTED) Color(0xFFBBF7D0) else Color(0xFFFECDD3)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("pairing_status_banner")
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    if (pairing.status == PairingStatus.CONNECTED) Color(0xFF16A34A) else Color(0xFFE11D48)
                                )
                        ) {
                            Icon(
                                imageVector = if (pairing.status == PairingStatus.CONNECTED) Icons.Default.Favorite else Icons.Default.PersonAdd,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (pairing.status == PairingStatus.CONNECTED) "Terhubung Saling Menyetujui" else "Belum Terhubung",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (pairing.status == PairingStatus.CONNECTED) Color(0xFF166534) else Color(0xFF9F1239)
                            )
                            Text(
                                text = if (pairing.status == PairingStatus.CONNECTED)
                                    "Pasangan: ${pairing.partnerName} (${pairing.partnerEmail})"
                                else
                                    "Masukkan email pasangan untuk mulai terhubung",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (pairing.status == PairingStatus.CONNECTED) Color(0xFF15803D) else Color(0xFFBE123C)
                            )
                        }
                    }

                    if (pairing.status == PairingStatus.CONNECTED) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Kode Pairing: ${pairing.pairingCode}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF166534)
                            )
                            OutlinedButton(
                                onClick = onDisconnectPartner,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                                modifier = Modifier.testTag("disconnect_partner_button")
                            ) {
                                Icon(Icons.Default.LinkOff, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Putus Tautan", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // Connect by Email Card
        item {
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Hubungkan / Ganti Email Pasangan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tautan hanya akan aktif jika kedua belah pihak saling menyetujui di aplikasi masing-masing.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = partnerEmailInput,
                        onValueChange = { partnerEmailInput = it },
                        label = { Text("Email Pasangan") },
                        placeholder = { Text("contoh: pasangan@gmail.com") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("partner_email_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (partnerEmailInput.isNotBlank()) {
                                onConnectPartner(partnerEmailInput.trim())
                                partnerEmailInput = ""
                            }
                        },
                        enabled = partnerEmailInput.isNotBlank(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("send_pairing_invite_button")
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Hubungkan Akun Pasangan")
                    }
                }
            }
        }

        // Update My Custom Status & Activity
        item {
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Perbarui Status & Aktivitas Anda",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Status ini langsung terlihat oleh pasangan di beranda.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = myStatusInput,
                        onValueChange = { myStatusInput = it },
                        label = { Text("Kabar Singkat / Status") },
                        placeholder = { Text("Misal: Di kantor sampai jam 5 sore") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("my_status_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Pilih Mode Aktivitas Gerak:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ActivityChip(
                            label = "Kerja",
                            isSelected = selectedActivity == ActivityType.AT_WORK,
                            onClick = { selectedActivity = ActivityType.AT_WORK },
                            modifier = Modifier.weight(1f)
                        )
                        ActivityChip(
                            label = "Rumah",
                            isSelected = selectedActivity == ActivityType.AT_HOME,
                            onClick = { selectedActivity = ActivityType.AT_HOME },
                            modifier = Modifier.weight(1f)
                        )
                        ActivityChip(
                            label = "Jalan",
                            isSelected = selectedActivity == ActivityType.WALKING,
                            onClick = { selectedActivity = ActivityType.WALKING },
                            modifier = Modifier.weight(1f)
                        )
                        ActivityChip(
                            label = "Kendaraan",
                            isSelected = selectedActivity == ActivityType.DRIVING,
                            onClick = { selectedActivity = ActivityType.DRIVING },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            onUpdateMyStatus(myStatusInput, selectedActivity)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_my_status_button")
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Simpan & Kirim Status ke Pasangan")
                    }
                }
            }
        }

        // Privacy & Mutual Permissions Card
        item {
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Izin Berbagi Anda",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))

                    PermissionToggleRow(
                        title = "Berbagi Lokasi GPS",
                        subtitle = "Izinkan pasangan melihat titik lokasi Anda",
                        checked = me.isSharingLocation,
                        onCheckedChange = onToggleLocationSharing
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PermissionToggleRow(
                        title = "Berbagi Info Baterai Ponsel",
                        subtitle = "Tampilkan persentase dan status pengisian daya",
                        checked = me.isSharingBattery,
                        onCheckedChange = onToggleBatterySharing
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PermissionToggleRow(
                        title = "Mode Privasi (Ghost Mode)",
                        subtitle = "Jeda pengiriman lokasi tanpa memutuskan ikatan",
                        checked = me.isGhostMode,
                        onCheckedChange = onToggleGhostMode
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ActivityChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun PermissionToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(text = subtitle, fontSize = 11.sp, color = Color(0xFF64748B))
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
