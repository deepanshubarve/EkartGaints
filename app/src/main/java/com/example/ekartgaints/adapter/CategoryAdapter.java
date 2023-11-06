package com.example.ekartgaints.adapter;

import android.content.Context;
import android.graphics.Color;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ekartgaints.R;
import com.example.ekartgaints.databinding.ItemCatogoriesBinding;
import com.example.ekartgaints.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>  {

    Context context;
    ArrayList<Category> categories;
    public CategoryAdapter(Context context,ArrayList<Category> categories){
        this.context = context;
        this.categories = categories;
    }
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_catogories,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
     Category category = categories.get(position);
     holder.binding.lable.setText(category.getName());
        Glide.with(context).load(category.getIcon())
                .into(holder.binding.imageView);

        holder.binding.imageView.setBackgroundColor(Color.parseColor(category.getColor()));
    }

    @Override
    public int getItemCount() {

        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        ItemCatogoriesBinding binding;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCatogoriesBinding.bind(itemView);



        }
    }

}