package com.smart.smartble.smartBle;

import android.bluetooth.BluetoothDevice;

import java.io.Serializable;

public class BleDevice implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6907330081273505841L;
	private BluetoothDevice device;
	private int rssi;
	private String key;

	public BluetoothDevice getDevice() {
		return device;
	}

	public void setDevice(BluetoothDevice device) {
		this.device = device;
	}

	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@SuppressWarnings("unused")
	private BleDevice() {

	}

	public BleDevice(BluetoothDevice device, int rssi) {
		this.device = device;
		this.rssi = rssi;
	}

}
