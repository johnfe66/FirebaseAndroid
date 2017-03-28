package com.johnfe.firebaseandroid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Johnfe Vargas on 2017-03-28.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{


    Context context;
    String [] values;
    View view;
    ViewHolder viewHolder;

    public RecyclerViewAdapter(Context context, String[] values) {
        this.context = context;
        this.values = values;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.single_item,parent,false);
        viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {

        holder.tv.setText(values[position]);

    }

    @Override
    public int getItemCount() {
        return values.length;
    }

    public static  class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public  ViewHolder(View itemView){

             super(itemView);
            tv= (TextView) itemView.findViewById(R.id.txtTextViewCard);
        }



    }
}
