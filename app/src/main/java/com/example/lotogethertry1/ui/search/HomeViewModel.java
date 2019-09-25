package com.example.lotogethertry1.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;

public class HomeViewModel extends ViewModel {

    MutableLiveData<List<Map<String,Object>>> mlist;
    MutableLiveData<List<String>> his_list;
    MutableLiveData<Boolean> search_flag;

    public HomeViewModel() {
        mlist=new MutableLiveData<>();
        his_list=new MutableLiveData<>();
        search_flag=new MutableLiveData<>();
        search_flag.setValue(false);
    }

    LiveData<List<Map<String,Object>>> getList(){
        return mlist;
    }
    LiveData<List<String>> getHis(){
        return his_list;
    }
    LiveData<Boolean> getflag(){
        return search_flag;
    }
}