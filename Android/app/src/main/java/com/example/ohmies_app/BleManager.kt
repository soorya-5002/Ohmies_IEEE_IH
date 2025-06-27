package com.example.ohmies_app

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission

class BleManager(private val context: Context) {

    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    // We'll use this to stop the scan later
    private var scanCallback: ScanCallback? = null

    /**
     * Starts scanning for BLE devices and triggers the callback when a matching device is found.
     */
    @RequiresPermission(android.Manifest.permission.BLUETOOTH_SCAN)
    @SuppressLint("MissingPermission")
    fun startScan(onDeviceFound: (BluetoothDevice) -> Unit) {
        val scanner = bluetoothAdapter?.bluetoothLeScanner

        if (scanner == null) {
            Log.e("BLE", "Bluetooth scanner not available")
            return
        }

        scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val device = result.device
                Log.d("BLE_SCAN", "Found: ${device.name} - ${device.address}")
                if (!device.name.isNullOrEmpty() && device.name.contains("ESP", ignoreCase = true)) {
                    onDeviceFound(device)
                }
            }

            override fun onScanFailed(errorCode: Int) {
                Log.e("BLE_SCAN", "Scan failed with error: $errorCode")
            }
        }

        scanner.startScan(scanCallback)
    }

    /**
     * Stops the BLE scan if it's running.
     */
    @SuppressLint("MissingPermission")
    fun stopScan() {
        bluetoothAdapter?.bluetoothLeScanner?.apply {
            scanCallback?.let {
                stopScan(it)
                Log.d("BLE_SCAN", "Scan stopped")
            }
        }
        scanCallback = null
    }
}
