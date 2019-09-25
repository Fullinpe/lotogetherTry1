package com.example.lotogethertry1;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.lotogethertry1.ui.search.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Handler handler=new Handler();
    public static DatagramSocket socket;
    private boolean flag=true;
    private Dialog dialog;
    private int rest_min;
    public static String box_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        //创建全局socket
        try {
            socket=new DatagramSocket(9995);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        dialog=new Dialog(MainActivity.this);
        ProgressBar progressBar = new ProgressBar(MainActivity.this, null, android.R.attr.progressBarStyleLarge);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        dialog.setContentView(progressBar);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        refresh();
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramPacket packet;
                byte[] content = new byte[1024];
                packet=new DatagramPacket(content,content.length);
                while (flag){
                    try {
                        socket.receive(packet);
                        final String string=new String(packet.getData(),packet.getOffset(),packet.getLength());
                        if(string.matches("[0-9]"))
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(string.equals("1"))
                                        constance.maketoast(MainActivity.this,"连接元件柜成功-"+string);
                                    else if(string.equals("2"))
                                        constance.maketoast(MainActivity.this,"关闭元件柜成功-"+string);
                                }
                            });
                        else
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                        constance.maketoast(MainActivity.this,"通讯故障-"+string);
                                }
                            });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    void refresh(){
        //获取服务器信息并存入constance
        new Thread(new Runnable() {
            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });
                int temp=DBUtils._DB("INSERT INTO users VALUES('"+getLocalMac()+"',0)");
                if(temp==1)
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            constance.maketoast(MainActivity.this,"欢迎新用户，请先联系管理员");
                        }
                    });
                String[][] strings=DBUtils.select_DB("SELECT * FROM component","component","num","box_id");
                if(strings!=null){
                    constance.clist1 =new ArrayList<>();
                    constance.clist2 =new ArrayList<>();
                    constance.box_id =new ArrayList<>();
                    for(int i=0;i<strings.length;i++) {
                        constance.clist1.add(strings[i][0]);
                        constance.clist2.add(Integer.parseInt(strings[i][1]));
                        constance.box_id.add(Integer.parseInt(strings[i][2]));
                        Log.e("TAG",strings[i][0]+"=="+Integer.parseInt(strings[i][1]));
                    }
                }else
                    Log.e("TAG","结果为null");

                strings=DBUtils.select_DB("SELECT component FROM `logs` WHERE device='"+getLocalMac()+"' AND num=0"
                        +" UNION ALL SELECT rest_min FROM users WHERE device='"+getLocalMac()+"'"
                        +" UNION ALL SELECT ip FROM ip","component");
                if(strings!=null){
                    constance.his_qurey =new ArrayList<>();
                    for(int i=0;i<strings.length;i++) {
                        Log.e("TAG",strings[i][0]);
                        if(i==strings.length-2){
                            rest_min=Integer.parseInt(strings[i][0]);
                            continue;
                        }else if(i==strings.length-1) {
                            box_ip=strings[i][0];
                            break;
                        }
                        constance.his_qurey.add(strings[i][0]);
                    }
                    HomeFragment.stop_flag=false;
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        constance.maketoast(MainActivity.this,"获取数据完成");
                        dialog.dismiss();
                    }
                });
            }
        }).start();
    }
    /**
     * 获取设备MAC 地址 由于 6.0 以后 WifiManager 得到的 MacAddress得到都是 相同的没有意义的内容
     * 所以采用以下方法获取Mac地址
     */
    public static String getLocalMac() {
        String macAddress;
        StringBuilder buf = new StringBuilder();
        NetworkInterface networkInterface;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "";
        }
        return macAddress;
    }
    public static void logs_get(final String component, final int num)
    {
        if(!getLocalMac().equals(""))
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DBUtils._DB("INSERT INTO `logs` (device,component,num) VALUES ('"+getLocalMac()+"','"+component+"','"+num+"')");
                }
            }).start();
        else
            Log.e("mac","无法获取Mac地址");
    }
    public static void logs_his(final String component)
    {
        if(!getLocalMac().equals(""))
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String[][] strings=DBUtils.select_DB("SELECT component FROM `logs` WHERE device='"+getLocalMac()+"' AND component='"+component+"'","device");
                    if(strings.length==0)
                        DBUtils._DB("INSERT INTO `logs` (device,component,num) VALUES ('"+getLocalMac()+"','"+component+"','0')");
                }
            }).start();
        else
            Log.e("mac","无法获取Mac地址");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.close();
        flag=false;
        HomeFragment.stop_flag=false;
    }
}
