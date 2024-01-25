package com.example.ekartgaints.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ekartgaints.R;
import com.example.ekartgaints.activities.ProductDetail;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product= products.get(position);

        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.productImage);
        holder.binding.productBrief.setText(product.getName());
        holder.binding.price.setText("IND" + product.getPrice());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetail.class);
            intent.putExtra("name",product.getName());
            intent.putExtra("image",product.getPrice());
            intent.putExtra("id",product.getId());
            intent.putExtra("price",product.getPrice());
            context.startActivity(intent);
        });
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
