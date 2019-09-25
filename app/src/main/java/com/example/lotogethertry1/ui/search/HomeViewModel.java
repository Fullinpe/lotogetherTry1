package com.example.lotogethertry1.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;

public class HomeViewModel extends ViewModel {

    MutableLiveData<List<Map<String,Object>>> mlist;

    public HomeViewModel() {
        mlist=new MutableLiveData<>();
    }

    LiveData<List<Map<String,Object>>> getList(){
        return mlist;
    }
}