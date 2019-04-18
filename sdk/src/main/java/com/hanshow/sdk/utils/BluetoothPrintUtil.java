package com.hanshow.sdk.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.text.TextUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * 蓝牙打印机工具类
 *
 * @author Dell
 * @date 2018/6/20
 */

public class BluetoothPrintUtil {

    //创建蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothDevice mBluetoothDevice = null;
    private BluetoothSocket mBluetoothSocket = null;
    OutputStream mOutputStream = null;
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Set<BluetoothDevice> pairedDevices = null;
    private final String PRINT_NAME = "Printer001";//蓝牙打印机的名称根据实际情况做修改


    private static BluetoothPrintUtil printUtil;

    private BluetoothPrintUtil(Context context) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }


    public static synchronized BluetoothPrintUtil getPrintUtil(Context context) {
        if (printUtil == null) {
            synchronized (BluetoothPrintUtil.class) {
                if (printUtil == null) {
                    printUtil = new BluetoothPrintUtil(context);
                }
            }
        }
        return printUtil;
    }


    /**
     * 获取已经配对的蓝牙打印机
     */
    public boolean getBluetoothPrint() {
        try {
            if (mBluetoothAdapter == null) {
                return false;
            }
            if (mBluetoothAdapter.isEnabled()) {
                pairedDevices = mBluetoothAdapter.getBondedDevices();
                if (pairedDevices.size() == 0) {
                    return false;
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 连接打印机
     */
    public boolean connectPrint() {
        try {
            boolean result = false;
            for (BluetoothDevice device : pairedDevices) {
                if (PRINT_NAME.equals(device.getName())) {
                    mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(device.getAddress());
                    mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(SPP_UUID);
                    if (!mBluetoothSocket.isConnected()){
                        mBluetoothSocket.connect();
                        result = true;
                    }
                }
            }
            return result;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 打印一行文本
     *
     * @param content 文本内容
     */
    public void printString(String content) {
        try {
            if (!TextUtils.isEmpty(content)) {
                mOutputStream = mBluetoothSocket.getOutputStream();
                mOutputStream.write((content + "\n").getBytes("GBK"));
                mOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 切纸
     */
    public void cutPaper() {
        try {
            mOutputStream = mBluetoothSocket.getOutputStream();
            mOutputStream.write(("\n").getBytes("GBK"));
            mOutputStream.write(("\n").getBytes("GBK"));
            mOutputStream.write(("\n").getBytes("GBK"));
            mOutputStream.write(new byte[]{0x0a, 0x0a, 0x1d, 0x56, 0x01});
            mOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
