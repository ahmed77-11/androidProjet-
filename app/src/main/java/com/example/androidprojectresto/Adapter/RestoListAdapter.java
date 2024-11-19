package com.example.androidprojectresto.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.androidprojectresto.Activity.DetailActivity;
import com.example.androidprojectresto.Domain.Resto;
import com.example.androidprojectresto.Domain.Time;
import com.example.androidprojectresto.R;

import java.util.ArrayList;
import java.util.HashMap;


public class RestoListAdapter extends RecyclerView.Adapter<RestoListAdapter.viewholder> {

    ArrayList<Resto> items;
    Context context;
    private HashMap<Integer, String> timeMap;


    public RestoListAdapter(ArrayList<Resto> items, ArrayList<Time> times) {
        this.items = items;
        this.timeMap = new HashMap<>();
        // Populate the timeMap
        for (Time time : times) {
            timeMap.put(time.getId(), time.getValue());
        }

    }



    @NonNull
    @Override
    public RestoListAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context=parent.getContext();
        View infalte= LayoutInflater.from(context).inflate(R.layout.viewholder_list_resto,parent,false);
        return new viewholder(infalte);
    }

    @Override
    public void onBindViewHolder(@NonNull RestoListAdapter.viewholder holder, int position) {
        Resto resto=items.get(position);
        holder.titleTxt.setText(resto.getTitle());
        holder.budgetTxt.setText(resto.getBudget());
        holder.rateTxt.setText(String.valueOf(resto.getStar()));
        int timeId = resto.getTimeId();
        String timeValue = timeMap.get(timeId);
        if (timeValue != null) {
            holder.timeTxt.setText(timeValue);
        } else {
            holder.timeTxt.setText("N/A"); // Default value if time is not found
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object", items.get(position));
                context.startActivity(intent);
            }
        });
            Glide.with(context)
                .load(resto.getImagePath())
                .transform(new CenterCrop(),new RoundedCorners(30))
                .into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView titleTxt,budgetTxt,rateTxt,timeTxt;
        ImageView pic;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            titleTxt=itemView.findViewById(R.id.titleTxt);
            budgetTxt=itemView.findViewById(R.id.budgetTxt);
            rateTxt=itemView.findViewById(R.id.rateTxt);
            timeTxt=itemView.findViewById(R.id.timeTxt);
            pic=itemView.findViewById(R.id.img);

        }
    }
}
