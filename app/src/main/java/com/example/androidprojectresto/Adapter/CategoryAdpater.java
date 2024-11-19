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
import com.example.androidprojectresto.Activity.ListRestosActivity;
import com.example.androidprojectresto.Domain.Category;
import com.example.androidprojectresto.Domain.Resto;
import com.example.androidprojectresto.Domain.Time;
import com.example.androidprojectresto.R;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryAdpater extends RecyclerView.Adapter<CategoryAdpater.ViewHolder> {

    private ArrayList<Category> items;
    private Context context;


    // Constructor that accepts both items and a list of times
    public CategoryAdpater(ArrayList<Category> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = items.get(position);

        holder.titleTxt.setText(category.getName());


        holder.pic.setBackgroundResource(R.drawable.cat_1_background);
        int drawbaleResourceId=context.getResources().getIdentifier(category.getImagePath(),"drawable",holder.pic.getContext().getPackageName());
        // Load image with Glide
        Glide.with(context)
                .load(drawbaleResourceId)
                .into(holder.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context, ListRestosActivity.class);
            intent.putExtra("CategoryId",items.get(position).getId());
            Log.i("fffffffff", String.valueOf(category.getId()));
            intent.putExtra("CategoryName",category.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.CatNameTxt);
            pic = itemView.findViewById(R.id.imgCat);
        }
    }
}
