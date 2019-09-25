package com.example.lotogethertry1;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class constance {
    public static List<String> his_qurey=new ArrayList<>();
    public static List<String> clist1=new ArrayList<>();
    public static List<Integer> clist2=new ArrayList<>();
    public static List<Integer> box_id=new ArrayList<>();
    public static void maketoast(Context context,String msg){
        Toast toast=Toast.makeText(context,msg,Toast.LENGTH_LONG);
        toast.setText(msg);
        toast.show();
    }
}
