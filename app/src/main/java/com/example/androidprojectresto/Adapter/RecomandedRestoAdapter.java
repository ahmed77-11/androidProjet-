package com.example.androidprojectresto.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

public class RecomandedRestoAdapter extends RecyclerView.Adapter<RecomandedRestoAdapter.ViewHolder> {

    private ArrayList<Resto> items;
    private Context context;

    private HashMap<Integer, String> timeMap;

    // Constructor that accepts both items and a list of times
    public RecomandedRestoAdapter(ArrayList<Resto> items, ArrayList<Time> times) {
        this.items = items;
        this.timeMap = new HashMap<>();
        // Populate the timeMap
        for (Time time : times) {
            timeMap.put(time.getId(), time.getValue());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recomanded_resto, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Resto resto = items.get(position);

        // Set the title, budget, and star rating
        holder.titleTxt.setText(resto.getTitle());

        holder.budgetTxt.setText(resto.getBudget());
        holder.starTxt.setText(String.valueOf(resto.getStar()));
        Log.i("piccccc", resto.getTitle());
        Log.i("piccccc",String.valueOf( resto.getStar()));
        Log.i("piccccc", resto.getBudget());
        Log.i("piccccc", (String) holder.titleTxt.getText());
        Log.i("piccccc", holder.pic.toString());

        // Set the time value if it exists in timeMap
        int timeId = resto.getTimeId();
        String timeValue = timeMap.get(timeId);
        if (timeValue != null) {
            holder.timeTxt.setText(timeValue);
        } else {
            holder.timeTxt.setText("N/A"); // Default value if time is not found
        }

        // Load image with Glide
        Glide.with(context)
                .load(resto.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(40))
                .into(holder.pic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object",items.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, budgetTxt, starTxt, timeTxt;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            budgetTxt = itemView.findViewById(R.id.BudgetTxt);
            starTxt = itemView.findViewById(R.id.starTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
