package com.example.myble;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 100;

    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 0. 저전력 블루투스의 가용성을 지정, 만약 ble 지원이 안된다면 ble기능을 비활성화 해야함
        // 매니페스트에 android.hardware.bluetooth_le를 false 해두면 ble 사용가능 여부 확인하는게 좋다
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // 1. BluetoothAdapter(블루투스 송수신 장치) 가져오기
        // 모든 블루투스 Activity에는 BluetoothAdapter가 필요

        //  getSystemService()를 사용하여 BluetoothManager의 인스턴스를 반환한 다음, 어댑터를 가져옴
       final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        // 2. ble 활성화 검사
        //  isEnabled()를 호출하여 현재 블루투스가 활성화되었는지 확인 : 이 메서드가 false를 반환하는 경우 블루투스가 비활성화된 것
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            // ble가 지원되지만 비활성화인 경우 사용자가 앱을 떠나지 않은 상태에서 ble를 활성화 하도록 요청 메세지 띄움(BluetoothAdapter 사용)
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            // 사용자가 비활성화 하였을 경우 액티비티 닫
            if (resultCode != Activity.RESULT_OK){
                finish();
                return;
            }
        }
    }
}