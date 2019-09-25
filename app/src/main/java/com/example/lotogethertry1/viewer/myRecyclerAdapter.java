package com.example.lotogethertry1.viewer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lotogethertry1.R;

import java.util.List;
import java.util.Map;

public class myRecyclerAdapter extends RecyclerView.Adapter<myRecyclerAdapter.InnerHolder> {

    private Context context;
    private List<Map<String,Object>> list;
    private View inflater;

    public myRecyclerAdapter(Context context, List<Map<String,Object>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        inflater = LayoutInflater.from(context).inflate(R.layout.recycle_item,parent,false);
        InnerHolder myViewHolder = new InnerHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        //将数据和控件绑定
        Map<String,Object> map=list.get(position);
        holder.textView.setText((String) map.get("name"));
        holder.textView1.setText(String.valueOf((int)map.get("num")));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        TextView textView,textView1;
        public InnerHolder(@NonNull final View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            textView1=itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(),"dianji"+getAdapterPosition(),Toast.LENGTH_LONG).show();
                    NavController controller= Navigation.findNavController(v);
                    Bundle bundle=new Bundle();
                    Map<String,Object> map=list.get(getAdapterPosition());
                    bundle.putString("name",(String) map.get("name"));
                    bundle.putInt("num",(int) map.get("num"));
                    controller.navigate(R.id.action_navigation_home_to_navigation_dashboard,bundle);
                }
            });
        }

    }
}
