package com.example.ekartgaints.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.PixelCopy;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ekartgaints.PaymentActivity;
import com.example.ekartgaints.R;
import com.example.ekartgaints.adapter.CartAdapter;
import com.example.ekartgaints.databinding.ActivityCheckoutBinding;
import com.example.ekartgaints.model.Product;
import com.example.ekartgaints.utils.Constants;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    ActivityCheckoutBinding binding;
    CartAdapter adapter;
    ArrayList<Product> products;
    double totalPrice = 0;
    final int tax = 11;
    ProgressDialog progressDialog;
    Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing......");

        products = new ArrayList<>();

        cart = TinyCartHelper.getCart();

        for(Map.Entry<Item,Integer> item : cart.getAllItemsWithQty().entrySet() ){
            Product product =(Product) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);

            products.add(product);
        }

        adapter = new CartAdapter(this, products, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                binding.Subtotal.setText(String.format("INR % 2f",cart.getTotalPrice()));
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,layoutManager.getOrientation());
        binding.cartlist.setLayoutManager(layoutManager);
        binding.cartlist.addItemDecoration(itemDecoration);
        binding.cartlist.setAdapter(adapter);

        binding.Subtotal.setText(String.format("INR % 2f",cart.getTotalPrice()));

        totalPrice = (cart.getTotalPrice().doubleValue() * tax/100) + cart.getTotalPrice().doubleValue();
        binding.total.setText("INR " + totalPrice);

        binding.checkoutButton.setOnClickListener(v -> {
            ProcessOrder();
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void ProcessOrder(){
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject productOrder = new JSONObject();
        JSONObject dataObject = new JSONObject();
        try {
         productOrder.put("address",binding.Addressbox.getText().toString());
            productOrder.put("buyer",binding.Name.getText().toString());
            productOrder.put("comment",binding.Comments.getText().toString());
            productOrder.put("created_at", Calendar.getInstance().getTimeInMillis());
            productOrder.put("last_update", Calendar.getInstance().getTimeInMillis());
            productOrder.put("date_ship", Calendar.getInstance().getTimeInMillis());
            productOrder.put("email",binding.EmailBox.getText().toString());
            productOrder.put("phone",binding.Phonebox.getText().toString());
            productOrder.put("serial","cab8c1a4e4421a3b");
            productOrder.put("shipping","");
            productOrder.put("shipping_location","");
            productOrder.put("shipping_rate","");
            productOrder.put("status","waiting");
            productOrder.put("tax",tax);
            productOrder.put("total_fees",totalPrice);


            JSONArray product_order_detail = new JSONArray();
            for(Map.Entry<Item,Integer> item : cart.getAllItemsWithQty().entrySet() ){
                Product product =(Product) item.getKey();
                int quantity = item.getValue();
                product.setQuantity(quantity);

                JSONObject productObj = new JSONObject();
                productObj.put("amount ",quantity);
                productObj.put("price_item ",product.getPrice());
                productObj.put("product_id ",product.getId());
                productObj.put("product_name ",product.getName());
                product_order_detail.put(productObj);

                dataObject.put("product_order",productOrder);
                dataObject.put("product_order_detail",product_order_detail);
            }

        }catch (JSONException e){}

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.POST_ORDER_URL,
                dataObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {


                    if (response.getString("status").equals("success")) {
                        Toast.makeText(CheckoutActivity.this, "Success Order", Toast.LENGTH_SHORT).show();
                        String orderNumber = response.getJSONObject("data").getString("code");


                        new AlertDialog.Builder(CheckoutActivity.this)
                                .setTitle("Order Successfull")
                                .setMessage("Your order number is " + orderNumber)
                                .setCancelable(false)
                                .setPositiveButton("Pay Now", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(CheckoutActivity.this, PaymentActivity.class);
                                        startActivity(intent);
                                        intent.putExtra("order code",orderNumber);
                                    }
                                }).show();

                        Log.e("res", response.toString());
                    } else {

                        new AlertDialog.Builder(CheckoutActivity.this)
                                .setTitle("Order Failed")
                                .setMessage("Something went wrong, please try again" )
                                .setCancelable(false)
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                        Toast.makeText(CheckoutActivity.this, "Failed Order", Toast.LENGTH_SHORT).show();
                        Log.e("res", response.toString());
                    }
                    progressDialog.dismiss();
                }catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("Security","secure_code");
                return headers;
            }
        };
        queue.add(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}