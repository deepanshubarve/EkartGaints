package com.example.ekartgaints.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

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
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        binding.searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("query",text.toString());
                startActivity(intent);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

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
        categoryAdapter = new CategoryAdapter(this,categories);
        getCategories();

        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        binding.catogariesList.setLayoutManager(layoutManager);
        binding.catogariesList.setAdapter(categoryAdapter );

    }



    void getCategories(){

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Remdi",response);
                    JSONObject mainObject = new JSONObject(response);
                    if(mainObject.getString("status").equals("success")){
                        JSONArray categoriesArray = mainObject.getJSONArray("categories");
                        for(int i =0;i<categoriesArray.length();i++){
                            JSONObject object = categoriesArray.getJSONObject(i);
                            Category category = new Category(object.getString("name"),
                            Constants.PRODUCTS_IMAGE_URL+object.getString("icon"),
                            object.getString("color"),
                            object.getString("brief"),
                                    object.getInt("id")
                            );
                            categories.add(category);
                        }
                        categoryAdapter.notifyDataSetChanged();

                    }else{
                        //Do nothing
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
    }
    
    void getRecentProduct(){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.GET_PRODUCTS_URL + "?count=10";
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

    void getRecentOffers(){


        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET,Constants.GET_OFFERS_URL,response ->{
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray offerArray = object.getJSONArray("news_info");
                    for(int i =0 ; i<offerArray.length();i++){
                        JSONObject childObj = offerArray.getJSONObject(i);
                        binding.carousel.addData(
                                new CarouselItem(
                                        Constants.NEWS_IMAGE_URL + childObj.getString("image"),
                                        childObj.getString("title")
                                )
                        );
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        },error -> {} );

        queue.add(request);

    }

    void initiProducts(){

        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this,products);
        getRecentProduct();
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.productlist.setLayoutManager(layoutManager);
        binding.productlist.setAdapter(productAdapter);
    }


}