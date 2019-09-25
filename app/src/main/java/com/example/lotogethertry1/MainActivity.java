package com.example.lotogethertry1;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Handler handler=new Handler();

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

        /*

          获取服务器信息并存入constance*

          */
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String[][] strings=DBUtils.select_DB("SELECT * FROM component","component","num");
                if(strings!=null){
                    constance.clist1 =new ArrayList<>();
                    constance.clist2 =new ArrayList<>();
                    for(int i=0;i<strings.length;i++) {
                        constance.clist1.add(strings[i][0]);
                        constance.clist2.add(Integer.parseInt(strings[i][1]));
                        Log.e("TAG",strings[i][0]+Integer.parseInt(strings[i][1]));
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"获取数据完成",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }

}
