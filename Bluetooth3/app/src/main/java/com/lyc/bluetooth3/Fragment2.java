package com.lyc.bluetooth3;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

/**
 * 这个Fragement用来开启关闭蓝牙等设置
 */
public class Fragment2 extends Fragment {

    public Switch aSwitch;
    private BluetoothAdapter bluetoothAdapter;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment2,container,false);
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //textView = getActivity().findViewById(R.id.textView1);

        //初始化
        initialize();

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //开启蓝牙
                    Snackbar.make(buttonView, "正在打开蓝牙..", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    openBluetooth();

                }else {
                    //关闭蓝牙
                    Snackbar.make(buttonView, "正在关闭蓝牙..", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    bluetoothAdapter.disable();

                }
            }
        });
    }

    private void initialize(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        aSwitch = getActivity().findViewById(R.id.switch2);
        aSwitch.setChecked(isBluetoothEnable());
        if(isBluetoothEnable()){
            Snackbar.make(this.getView(), "蓝牙已打开", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else{
            Snackbar.make(this.getView(), "蓝牙未开启", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    /**
     * 判断蓝牙是否开启
     *
     * @return
     */
    public boolean isBluetoothEnable() {
        return bluetoothAdapter.isEnabled();
    }

    /**
     * 开启蓝牙,可被发现300秒
     *
     * @param
     * @param
     */
    public void openBluetooth() {
        final int REQUEST_ENABLE_BT = 1;

        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivityForResult(intent, REQUEST_ENABLE_BT);
    }
}
