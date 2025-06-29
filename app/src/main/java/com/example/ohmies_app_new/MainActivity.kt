package com.example.ohmies_app

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.activity.compose.setContent
import com.example.ohmies_app_new.BleManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()

            val bleManager = remember { BleManager(context) }
            val bleDevices = remember { mutableStateListOf<BluetoothDevice>() }

            // isScanning and scanCompleted states
            val isScanning = remember { mutableStateOf(false) }
            val scanCompleted = remember { mutableStateOf(false) }

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                // Handle permissions
            }

            LaunchedEffect(Unit) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_CONNECT
                        )
                    )
                } else {
                    permissionLauncher.launch(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                    )
                }
            }

            Surface(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF76C7FF), Color(0xFFFFE59D))
                            )
                        )
                ) {
                    // Centered BLE Scanner UI
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(bottom = 60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Ohmies", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("BLE Device Scanner", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(onClick = {
                            bleDevices.clear()
                            isScanning.value = true
                            scanCompleted.value = false

                            bleManager.startScan { device ->
                                if (!bleDevices.any { it.address == device.address }) {
                                    bleDevices.add(device)
                                }
                            }

                            coroutineScope.launch {
                                delay(10000)
                                bleManager.stopScan()
                                isScanning.value = false
                                scanCompleted.value = true
                            }
                        }) {
                            Text("Start Scanning")
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        if (isScanning.value) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Scanning for devices...")
                        } else if (scanCompleted.value && bleDevices.isEmpty()) {
                            Text("No devices found.")
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Text("Nearby ESP32 Devices:", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(10.dp))

                        if (bleDevices.isNotEmpty()) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                bleDevices.forEach { device ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(6.dp)
                                            .clickable { },
                                        elevation = 4.dp
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text(text = "Name: ${device.name ?: "Unknown"}")
                                            Text(text = "Address: ${device.address}")
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Bottom Navigation Bar
                    BottomNavigationBar(modifier = Modifier.align(Alignment.BottomCenter))
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier) {
    BottomNavigation(
        modifier = modifier,
        backgroundColor = Color(0xFFF5F5F5),
        contentColor = Color.Black
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            selected = false,
            onClick = { }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.EventSeat, contentDescription = "Seat") },
            selected = false,
            onClick = { }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.ShowChart, contentDescription = "Chart") },
            selected = true,
            onClick = { }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Trophy") },
            selected = false,
            onClick = { }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            selected = false,
            onClick = { }
        )
    }
}

@Composable
fun BLEScannerScreen(
    onStartScan: () -> Unit,
    devices: List<BluetoothDevice>,
    isScanning: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF76C7FF), Color(0xFFFFE59D))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text("Ohmies", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("BLE Device Scanner", fontSize = 18.sp, fontWeight = FontWeight.Medium)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { onStartScan() }) {
            Text("Start Scanning")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isScanning) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(10.dp))
            Text("Scanning for devices...")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Nearby ESP32 Devices:", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(10.dp))

        if (devices.isEmpty() && !isScanning) {
            Text("No devices found yet.")
        } else {
            Column(modifier = Modifier.fillMaxWidth()) {
                devices.forEach { device ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .clickable {
                                // TODO: Connect to this device in next step
                            },
                        elevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "Name: ${device.name ?: "Unknown"}")
                            Text(text = "Address: ${device.address}")
                        }
                    }
                }
            }
        }
    }
}
