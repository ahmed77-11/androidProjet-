package com.example.androidprojectresto.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidprojectresto.Activity.ListRestoAdminActivity;
import com.example.androidprojectresto.Activity.ModifResto;
import com.example.androidprojectresto.Domain.Resto;
import com.example.androidprojectresto.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RestoListAdminAdpater extends RecyclerView.Adapter<RestoListAdminAdpater.ViewHolder> {

    ArrayList<Resto> items;
    private OnDeleteListener onDeleteListener;
    Context context;


    public RestoListAdminAdpater(ArrayList<Resto> items, OnDeleteListener onDeleteListener) {
        this.items = items;
        this.onDeleteListener = onDeleteListener;
    }

    @NonNull
    @Override
    public RestoListAdminAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View inflate= LayoutInflater.from(context).inflate(R.layout.item_admin_restaurant,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RestoListAdminAdpater.ViewHolder holder, int position) {
        Resto resto=items.get(position);
        holder.nameTxt.setText(resto.getTitle());

        holder.btnMore.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.btnMore);
            popupMenu.inflate(R.menu.popup_menu);

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_modify) {
                    Intent i=new Intent(context, ModifResto.class);
                    i.putExtra("id",resto.getId());
                    i.putExtra("title",resto.getTitle());
                    i.putExtra("description",resto.getDescription());
                    i.putExtra("category",resto.getCategoryId());
                    i.putExtra("location",resto.getLocationId());
                    i.putExtra("time",resto.getTimeId());
                    i.putExtra("budget",resto.getBudget());
                    i.putExtra("imagePath",resto.getImagePath());
                    context.startActivity(i);
                    return true;
                }
                if (item.getItemId() == R.id.action_delete) {
                    Log.i("dd", String.valueOf(resto.getId()));
                    this.onDeleteListener.onDelete(resto.getId(),position);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        ImageButton btnMore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTxt=itemView.findViewById(R.id.tv_restaurant_name);
            btnMore=itemView.findViewById(R.id.btn_more_options);

        }
    }
    public interface OnDeleteListener {
        void onDelete(int restoId, int position);
    }
}
