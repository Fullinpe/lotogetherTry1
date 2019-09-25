package com.example.lotogethertry1.ui.get;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.lotogethertry1.DBUtils;
import com.example.lotogethertry1.MainActivity;
import com.example.lotogethertry1.R;
import com.example.lotogethertry1.constance;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class DashboardFragment extends Fragment {
    String name;
    int num,box_id;

    private DashboardViewModel dashboardViewModel;
    private DatagramSocket socket;
    private Handler handler=new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_get, container, false);
        TextView textView1=root.findViewById(R.id.item_name);
        final TextView textView2=root.findViewById(R.id.item_num);
        final EditText editText=root.findViewById(R.id.item_edit);
        ImageButton button=root.findViewById(R.id.imageButton);
        Bundle bundle=getArguments();
        if(bundle!=null){
            constance.maketoast(getActivity(),"监听注册成功");
            name=bundle.getString("name");
            num=bundle.getInt("num");
            box_id=bundle.getInt("box_id");
            textView1.setText(name);
            textView2.setText(String.valueOf(num));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final String[][] strings= DBUtils.select_DB("SELECT rest_min FROM users WHERE device='"+MainActivity.getLocalMac()+"'","rest_min");
                            if(strings!=null&&strings.length==1){
                                if(!strings[0][0].equals("0")&&!editText.getText().toString().equals("")&&Integer.parseInt(editText.getText().toString())!=0){
                                    gongneng(box_id+"",MainActivity.box_ip,9995);
                                    MainActivity.logs_get(name,Integer.parseInt(editText.getText().toString()));
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            num-=Integer.parseInt(editText.getText().toString());
                                            textView2.setText(num+"");
                                        }
                                    });
                                    Log.e("TAG",editText.getText().toString());
                                }else
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(strings[0][0].equals("0"))
                                                constance.maketoast(getActivity(),"你无权，请联系管理员");
                                            else
                                                constance.maketoast(getActivity(),"请选择获取数量");
                                        }
                                    });
                            }

                        }
                    }).start();

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
                    socket= MainActivity.socket;
                    hostAddress = InetAddress.getByName(ip);
                    String msgSend=strs;
                    DatagramPacket packetSend = new DatagramPacket(msgSend.getBytes(), msgSend.getBytes().length, hostAddress, udpPorts);
                    socket.send(packetSend);
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