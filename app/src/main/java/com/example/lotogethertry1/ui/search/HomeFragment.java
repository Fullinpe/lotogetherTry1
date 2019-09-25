package com.example.lotogethertry1.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


    private HomeViewModel homeViewModel;

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
                            list.add(map);
                        }
                        bool=false;
                    }
                }
                homeViewModel.mlist.setValue(list);
//                Log.e("TAG", String.valueOf(constance.clist1));
                return false;
            }
        });
        searchView.onActionViewExpanded();

    //---------
        return root;
    }
}