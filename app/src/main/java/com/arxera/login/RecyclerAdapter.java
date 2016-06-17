package com.arxera.login;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 15-06-2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_LIST = 1;
    ArrayList<get_data> array_list = new ArrayList<>();

    public RecyclerAdapter(ArrayList<get_data> array_list){
        this.array_list = array_list;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType==TYPE_HEAD){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_header,parent,false);
            RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view,viewType);
            return recyclerViewHolder;
        }else if (viewType==TYPE_LIST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
            RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view,viewType);
            return recyclerViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        if (holder.viewType==TYPE_LIST){
            get_data get_data = array_list.get(position-1);
            holder.name.setText(get_data.getName());
            holder.distance.setText(get_data.getDistance());
            holder.lat.setText(String.valueOf(get_data.getLat()));
            holder.longi.setText(String.valueOf(get_data.getLongi()));
        }
    }

    @Override
    public int getItemCount() {
        return array_list.size()+1;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView name,distance,lat,longi;
        int viewType;
        public RecyclerViewHolder(View view,int viewType){
            super(view);
            if (viewType==TYPE_LIST) {
                name = (TextView) view.findViewById(R.id.name);
                distance = (TextView) view.findViewById(R.id.distance);
                lat = (TextView) view.findViewById(R.id.lat);
                longi = (TextView) view.findViewById(R.id.longi);
                this.viewType = TYPE_LIST;
            }
            else if (viewType == TYPE_HEAD){
                this.viewType = TYPE_HEAD;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEAD;
            return TYPE_LIST;
    }
}
