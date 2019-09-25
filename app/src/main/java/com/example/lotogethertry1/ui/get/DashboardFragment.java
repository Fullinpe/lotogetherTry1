package com.example.lotogethertry1.ui.get;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.lotogethertry1.R;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class DashboardFragment extends Fragment {
    String name;
    int num;

    private DashboardViewModel dashboardViewModel;
    private DatagramSocket socket;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_get, container, false);
        TextView textView1=root.findViewById(R.id.item_name);
        TextView textView2=root.findViewById(R.id.item_num);
        final EditText editText=root.findViewById(R.id.item_edit);
        ImageButton button=root.findViewById(R.id.imageButton);
        Bundle bundle=getArguments();
        if(bundle!=null){
            name=bundle.getString("name");
            num=bundle.getInt("num");
            textView1.setText(name);
            textView2.setText(String.valueOf(num));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                    Toast.makeText(getActivity(),"监听注册成功",Toast.LENGTH_SHORT).show();
                    gongneng(getHostIP(),"10.22.236.141",9995);

                }
            });
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!editText.getText().toString().isEmpty())
                        if(Integer.parseInt(editText.getText().toString())>num) {
                            editText.setText(String.valueOf(num));
                            editText.setSelection(editText.getText().toString().length());
                        }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        return root;
    }
    public void gongneng(final String strs,final String ip, final int udpPorts){
        new Thread(new Runnable(){
            @Override
            public void run()
            {
                InetAddress hostAddress = null;
                try
                {
                    socket=new DatagramSocket();
                    hostAddress = InetAddress.getByName(ip);
                    String msgSend=strs;
                    DatagramPacket packetSend = new DatagramPacket(msgSend.getBytes(), msgSend.getBytes().length, hostAddress, udpPorts);
                    socket.send(packetSend);
                    socket.close();
                }
                catch (Exception e)
                {

                }
            }
        }).start();
    }

    public static String getHostIP() {
        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return hostIp;
    }

}