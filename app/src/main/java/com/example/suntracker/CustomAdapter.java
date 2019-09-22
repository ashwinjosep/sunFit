package com.example.suntracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<food_options> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textViewOption;
        TextView textViewCalorie;

        public MyViewHolder(View itemView){
            super(itemView);
            this.textViewOption = (TextView) itemView.findViewById(R.id.option_name);
            this.textViewCalorie = (TextView) itemView.findViewById(R.id.option_calorie);
        }
    }

    public CustomAdapter(ArrayList<food_options> data){
        this.dataSet=data;
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout, parent, false);

        view.setOnClickListener(OptionsActivity.optionClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        TextView textViewOption = holder.textViewOption;
        TextView textViewCalorie = holder.textViewCalorie;

        textViewOption.setText(dataSet.get(position).getOption());
        textViewCalorie.setText(dataSet.get(position).getCalorie() + " Cal");
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
