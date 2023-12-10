package com.example.ekartgaints.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ekartgaints.R;
import com.example.ekartgaints.databinding.ItemProductviewBinding;
import com.example.ekartgaints.model.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

    Context context;
    ArrayList<Product> products;

    public ProductAdapter(Context context,ArrayList<Product> products){
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_productview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product= products.get(position);

        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.ItemImage);
        holder.binding.lable.setText(product.getName());
        holder.binding.price.setText("IND" + product.getPrice());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        ItemProductviewBinding binding;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemProductviewBinding.bind(itemView);


        }
    }
}
