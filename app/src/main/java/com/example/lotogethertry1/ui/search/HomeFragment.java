package com.example.lotogethertry1.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lotogethertry1.FlowLayout;
import com.example.lotogethertry1.R;
import com.example.lotogethertry1.constance;
import com.example.lotogethertry1.viewer.myRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {


    private myRecyclerAdapter adapterDome;//声明适配器
    private Context context;
    private List<Map<String,Object>> list;


    public HomeViewModel homeViewModel;
    public static boolean stop_flag=true;
    private Handler handler=new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        final RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        final SearchView searchView=root.findViewById(R.id.searchView);

        homeViewModel.getList().observe(this, new Observer<List<Map<String, Object>>>() {
            @Override
            public void onChanged(List<Map<String, Object>> maps) {
                adapterDome = new myRecyclerAdapter(context,maps);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapterDome);

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                list = new ArrayList<>();
                Map<String, Object> map;
                if(!newText.isEmpty()) {
                    boolean bool=false;
                    homeViewModel.search_flag.setValue(true);
                    for (int x=0;x<constance.clist1.size();x++) {
                        for (int i=0;i<constance.clist1.get(x).length()-newText.length()+1;i++){
                            if(constance.clist1.get(x).regionMatches(true,i,newText,0,newText.length())){
                                bool=true;
                                break;
                            }
                        }
                        if(bool){
                            map = new HashMap<>();
                            map.put("name",constance.clist1.get(x));
                            map.put("num",constance.clist2.get(x));
                            map.put("box_id",constance.box_id.get(x));
                            list.add(map);
                        }
                        bool=false;
                    }
                }else
                    homeViewModel.search_flag.setValue(false);
                homeViewModel.mlist.setValue(list);
//                Log.e("TAG", String.valueOf(constance.clist1));
                return false;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });

        final FlowLayout flowLayout = root.findViewById(R.id.flowlayout);
        homeViewModel.getHis().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(final List<String> strings) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(15, 10, 10, 10);
                if (flowLayout != null) {
                    flowLayout.removeAllViews();
                }
                for (int i = 0; i < strings.size(); i++) {
                    TextView tv = new TextView(getActivity());
                    tv.setPadding(28, 10, 28, 10);
                    tv.setText(strings.get(i));
                    tv.setMaxEms(15);
                    tv.setSingleLine();
                    tv.setBackgroundResource(R.drawable.label_dra);
                    tv.setLayoutParams(layoutParams);
                    final int finalI = i;
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchView.onActionViewExpanded();
                            searchView.setQuery(strings.get(finalI),false);
                        }
                    });
                    assert flowLayout != null;
                    flowLayout.addView(tv, layoutParams);
                }
            }
        });
        homeViewModel.getflag().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    flowLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }else{
                    flowLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
        final List<String> list1=new ArrayList<>();
        for (int i = 0; i <10; i++) {
            list1.add("Android");
            list1.add("Java");
            list1.add("IOS");
            list1.add("python");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (stop_flag);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        homeViewModel.his_list.setValue(constance.his_qurey);
                    }
                });
            }
        }).start();

    //---------
        return root;
    }
}