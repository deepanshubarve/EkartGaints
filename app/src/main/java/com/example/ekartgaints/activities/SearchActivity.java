package com.example.ekartgaints.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ekartgaints.R;
import com.example.ekartgaints.adapter.ProductAdapter;
import com.example.ekartgaints.databinding.ActivitySearchBinding;
import com.example.ekartgaints.model.Product;
import com.example.ekartgaints.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    ProductAdapter productAdapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this,products);

        String query = getIntent().getStringExtra("query");

        getSupportActionBar().setTitle(query);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getProduct(query);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.productlist.setLayoutManager(layoutManager);
        binding.productlist.setAdapter(productAdapter);



    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    void getProduct(String query){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.GET_PRODUCTS_URL + "?q="+query;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);

                if(object.getString("status").equals("success") ){
                    JSONArray productsArray = object.getJSONArray("products");
                    for(int i = 0;i<productsArray.length();i++){
                        JSONObject chailObj = productsArray.getJSONObject(i);
                        Product product = new Product(chailObj.getString("name"),
                                Constants.CATEGORIES_IMAGE_URL + chailObj.getString("image"),
                                chailObj.getString("status"),
                                chailObj.getDouble("price"),
                                chailObj.getDouble("discount"),
                                chailObj.getInt("stock"),
                                chailObj.getInt("id"));
                        products.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {});
        queue.add(request);
    }
}