package com.example.ekartgaints.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ekartgaints.R;
import com.example.ekartgaints.databinding.ItemCartBinding;
import com.example.ekartgaints.databinding.QuantityCartBinding;
import com.example.ekartgaints.model.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;
import java.util.Objects;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewholder> {

    Context context;
    ArrayList<Product> products;
    CartListener cartListener;
    Cart cart;

    public interface CartListener{
        public void onQuantityChanged();

    }

    public CartAdapter(Context context,ArrayList<Product> products,CartListener cartListener){
        this.context = context;
        this.products = products;
        this.cartListener =cartListener;
       cart = TinyCartHelper.getCart();
    }
    

    @NonNull
    @Override
    public CartViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewholder(LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewholder holder, int position) {
     Product product = products.get(position);
        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.CartProductImage);

        holder.binding.name.setText(product.getName());
        holder.binding.priceincart.setText("INR" + product.getPrice());
        holder.binding.quantity.setText(product.getQuantity() + "items");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuantityCartBinding quantityCartBinding = QuantityCartBinding.inflate(LayoutInflater.from(context));
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setView(quantityCartBinding.getRoot())
                        .create();

                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));

                quantityCartBinding.productName.setText(product.getName());
                quantityCartBinding.productStock.setText(product.getStock());
                quantityCartBinding.quantity.setText(String.valueOf(product.getQuantity()));

                int stock = product.getStock();


                quantityCartBinding.plusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     int quantity = product.getQuantity();

                     quantity++;

                     if(quantity>product.getStock()){
                         Toast.makeText(context,"Max Stocks available" + product.getStock(),Toast.LENGTH_SHORT).show();
                         return;
                     }else{
                         product.setQuantity(quantity);
                         quantityCartBinding.quantity.setText(String.valueOf(quantity));
                     }
                        cart.updateItem(product,product.getQuantity());
                        cartListener.onQuantityChanged();
                        notifyDataSetChanged();

                    }
                });

                quantityCartBinding.minusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                   int quantity = product.getQuantity();
                   if(quantity >1)
                       quantity--;
                   product.setQuantity(quantity);
                   quantityCartBinding.quantity.setText(String.valueOf(quantity));

                        cart.updateItem(product,product.getQuantity());
                        cartListener.onQuantityChanged();
                        notifyDataSetChanged();
                    }
                });

                quantityCartBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      dialog.dismiss();
                      cart.updateItem(product,product.getQuantity());
                      cartListener.onQuantityChanged();
                      notifyDataSetChanged();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return products.size();
    }

    public class CartViewholder extends RecyclerView.ViewHolder{

        ItemCartBinding binding;

        public CartViewholder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCartBinding.bind(itemView);
            cartListener.onQuantityChanged();
        }
    }

}
