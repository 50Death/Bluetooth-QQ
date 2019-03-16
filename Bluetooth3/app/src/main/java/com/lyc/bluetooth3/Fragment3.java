package com.lyc.bluetooth3;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Fragment3 extends Fragment {

    private Button button;
    private EditText editText;

    private ListView listView;
    private ArrayAdapter<String> messages;

    private Fragment1 fragment1;

    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private BluetoothServerSocket mmServerSocket;

    private ConnectThread connectThread;

    //private RSACrypt rsaCrypt;
    private String rsaPublicKeyString;
    private String rsaPrivateKeyString;

    //private AESCrypt aesCrypt;
    private String aesKey;
    private String rsaedAESKey;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, container, false);
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initUI();
        initComponent();
        //initCrypt();
        //testCrypt();
        startConnect();

        //START OF TEST


        /*
        for (int i = 0; i <= 20; i++) {
            messages.add(i + "\n" + "发送成功");
            //messages.add("Hello！！" + "\n" + "解密成功");
        }
        */
        //END OF TEST

        if (mmSocket == null) {
            button.setText("刷新");
            getActivity().setTitle("未连接");
        } else {
            button.setText("发送");
            getActivity().setTitle("To: ");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mmSocket == null) {

                    if (!startConnect()) {
                        button.setText("刷新");
                        getActivity().setTitle("未连接");

                        Snackbar.make(v, "刷新失败，请检查连接状态", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } else {
                    button.setText("发送");
                    getActivity().setTitle("To: ");
                    String text = editText.getText().toString();
                    connectThread.write(text.getBytes());
                    editText.setText("");
                }
            }
        });
    }

    public void onResume() {
        super.onResume();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    private void initUI() {
        button = getActivity().findViewById(R.id.button2);
        editText = getActivity().findViewById(R.id.editText2);
        listView = getActivity().findViewById(R.id.lv_chat_dialog);
    }

    private void initComponent() {
        List<String> tmp1 = new ArrayList<>();
        messages = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, tmp1);
        listView.setAdapter(messages);
    }


    public boolean startConnect() {
        List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
        fragment1 = (Fragment1) fragments.get(0);
        mmSocket = fragment1.getMmSocket();
        mmDevice = fragment1.getMmDevice();
        mmServerSocket = fragment1.getMmServerSocket();
        if (mmSocket != null) {
            connectThread = new ConnectThread(mmSocket);
            connectThread.start();

            //TEST
            //connectThread.write("Hello There".getBytes());

            return true;
        } else {
            return false;
        }
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        private InputStream mmInStream;
        private OutputStream mmOutStream;

        public ConnectThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    //connectThread.write("Hello There".getBytes());
                    bytes = mmInStream.read(buffer);

                    //messages.add("收到: "+buffer);
                    new ConnectTask().execute("收到： " + new String(buffer, "UTF-8"));

                    System.err.println(buffer);
                    Log.i("!!!!!!!!!", new String(buffer));
                    // Send the obtained bytes to the UI activity
                    /*mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();*/
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
                mmOutStream.flush();
                messages.add("发送: " + new String(bytes,"UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }


    private class ConnectTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            return objects[0];
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         *
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param o The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            String s = (String) o;
            messages.add(s);
        }
    }
}
