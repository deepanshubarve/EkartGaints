package com.example.ekartgaints.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.widget.GridLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ekartgaints.R;
import com.example.ekartgaints.adapter.CategoryAdapter;
import com.example.ekartgaints.adapter.ProductAdapter;
import com.example.ekartgaints.databinding.ActivityMainBinding;
import com.example.ekartgaints.model.Category;
import com.example.ekartgaints.model.Product;
import com.example.ekartgaints.utils.Constants;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categories;

    ProductAdapter productAdapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initiCategories();
        initiProducts();
        initiSlider();



    }
       void initiSlider() {
        binding.carousel.addData(new CarouselItem("https://www.google.com/url?sa=i&url=https%3A%2F%2Fdepositphotos.com%2Fphotos%2Fspecial-offer.html&psig=AOvVaw3WwvpfH7ubDggopi8BIQAK&ust=1702065439562000&source=images&cd=vfe&ved=0CBIQjRxqFwoTCLiJqYGO_oIDFQAAAAAdAAAAABAe","ver"));
        binding.carousel.addData(new CarouselItem("https://cdn.pixabay.com/photo/2015/01/21/13/21/special-offer-606691_640.png","ter"));
    }

    void initiCategories(){
        categories = new ArrayList<>();
        categories.add(new Category("Sports & Outdore","","#0000ff","some brief",1));
        categories.add(new Category("Cluture & Outdore","","#0000ff","some brief",2));
        categories.add(new Category("Dance & Outdore","","#0000ff","some brief",3));
        categories.add(new Category("Outdore","","#0000ff","some brief",4));
        categories.add(new Category("babies","","#0000ff","some brief",5));
        categories.add(new Category("local mall","","#0000ff","some brief",6));
        categories.add(new Category("Electronics","","#0000ff","It is electronics",7));

        categoryAdapter = new CategoryAdapter(this,categories);






        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        binding.catogariesList.setLayoutManager(layoutManager);

    }

    void getCategories(){

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
    }

    void initiProducts(){

        products = new ArrayList<>();
        products.add(new Product("","","",500,90,12,7));
        productAdapter = new ProductAdapter(this,products);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.productlist.setLayoutManager(layoutManager);
        binding.productlist.setAdapter(productAdapter);
    }


}