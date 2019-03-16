package com.lyc.bluetooth3;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

public class Fragment1 extends Fragment {

    public Button button1;
    public Button button2;
    public ListView listView;

    private BluetoothAdapter bluetoothAdapter;
    private static final UUID xUUDI = UUID.fromString("d2ea0fdc-1982-40e1-98e8-9dcd45130b8e");
    private static final String NAME = "BluetoothChat";

    private ArrayAdapter<String> mArrayAdapter;
    private Vector<BluetoothDevice> devices = new Vector<>();

    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private BluetoothServerSocket mmServerSocket;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initUI();
        initComponent();
        initialize();
        //textView = getActivity().findViewById(R.id.textView1);
    }

    private void initialize() {
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBluetoothEnable()) {
                    //TODO
                    mArrayAdapter.clear();
                    devices.clear();
                    findDevice();
                    ConnectAsServer server = new ConnectAsServer();
                    server.start();
                } else {
                    Snackbar.make(v, "蓝牙未开启，请在“设置”中开启", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = devices.get(position);
                ShowDialog(device);
            }
        });
    }

    private void initUI() {
        button1 = getActivity().findViewById(R.id.button);
        button2 = getActivity().findViewById(R.id.button3);
        listView = getActivity().findViewById(R.id.listView);
    }

    private void initComponent() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        List<String> tmp1 = new ArrayList<>();
        mArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, tmp1);
        listView.setAdapter(mArrayAdapter);


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
     * 查找设备，并存入ArrayAdapter中
     */
    public void findDevice() {
        bluetoothAdapter.startDiscovery();

        //查询已配对设备
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                devices.add(device);
            }
        }

        //发现设备,广播

        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    devices.add(device);
                }
            }
        };
        //注册
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        try {
            getActivity().registerReceiver(mReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void ShowDialog(final BluetoothDevice device) {
        AlertDialog.Builder ad = new AlertDialog.Builder(this.getActivity());
        ad.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //连接
                ConnectAsClient client = new ConnectAsClient(device);
                client.run();
            }
        });

        ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        ad.setMessage("你确定要连接吗？");
        ad.setTitle("提示");
        ad.show();
    }


    private class ConnectAsClient extends Thread {


        public ConnectAsClient(BluetoothDevice device) {
            // Cancel discovery because it will slow down the connection
            bluetoothAdapter.cancelDiscovery();

            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                //tmp = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device,1);
                tmp = device.createRfcommSocketToServiceRecord(xUUDI);
                //tmp = device.createRfcommSocket(1);
            } /*catch (IOException e) {
                e.printStackTrace();
            }*/ catch (Exception e1) {
                e1.printStackTrace();
            }
            mmSocket = tmp;
        }

        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        try {

                            mmSocket.connect();

                            Snackbar.make(getView(), "连接成功！", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                            break;
                        } catch (IOException connectException) {
                            connectException.printStackTrace();

                            try {
                                mmSocket = (BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket",
                                        new Class[]{int.class}).invoke(mmDevice, 1);
                                mmSocket.connect();

                                Snackbar.make(getView(), "连接成功！", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                                break;
                            } catch (NoSuchMethodException e2) {
                                e2.printStackTrace();
                            } catch (IllegalAccessException e3) {
                                e3.printStackTrace();
                            } catch (InvocationTargetException e4) {
                                e4.printStackTrace();
                            } catch (IOException e5){
                                e5.printStackTrace();
                            }

                            /*
                            try {

                                mmSocket.close();
                            } catch (IOException closeException) {
                                closeException.printStackTrace();
                            }
                            return;
                            */
                        }
                    }
                    // Do work to manage the connection (in a separate thread)
                    /*
                    synchronized (this) {
                        connectThread = new ConnectThread(mmSocket);
                        connectThread.start();

                    }*/
                }
            }).start();
        }


        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectAsServer extends Thread {

        public ConnectAsServer() {
            BluetoothServerSocket tmp = null;
            try {
                //tmp = (BluetoothServerSocket) bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID).getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(bluetoothAdapter,1);
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, xUUDI);//PROBLEM
                //tmp = (BluetoothServerSocket)bluetoothAdapter.getClass().getMethod("listenUsingRfcommOn", new Class[]{int.class}).invoke(bluetoothAdapter,1);
            } catch (Exception e) {
            }
            mmServerSocket = tmp;
        }

        public void run() {
            //BluetoothSocket socket = null;
            while (true) {
                try {
                    mmSocket = mmServerSocket.accept();
                    Snackbar.make(getView(), "您已被对方连接成功！", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }

                if (mmSocket != null) {
                    /*
                    synchronized (this) {
                        connectThread = new ConnectThread(socket);
                        connectThread.start();
                    }*/
                    break;
                }
            }
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BluetoothSocket getMmSocket(){
        return this.mmSocket;
    }

    public BluetoothServerSocket getMmServerSocket(){
        return  this.mmServerSocket;
    }

    public BluetoothDevice getMmDevice(){
        return this.mmDevice;
    }

}
