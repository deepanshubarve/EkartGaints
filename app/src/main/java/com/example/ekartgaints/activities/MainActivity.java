package com.example.ekartgaints.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.widget.GridLayout;

import com.example.ekartgaints.R;
import com.example.ekartgaints.adapter.CategoryAdapter;
import com.example.ekartgaints.databinding.ActivityMainBinding;
import com.example.ekartgaints.model.Category;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        categories = new ArrayList<>();
        categories.add(new Category("Sports & Outdore","","#0000ff","some brief",1));
        categories.add(new Category("Cluture & Outdore","","#0000ff","some brief",2));
        categories.add(new Category("Dance & Outdore","","#0000ff","some brief",3));
        categories.add(new Category("Outdore","","#0000ff","some brief",4));
        categories.add(new Category("babies","","#0000ff","some brief",5));
        categories.add(new Category("local mall","","#0000ff","some brief",6));
        categories.add(new Category("Electronics","","#00ffff","It is electronics",7));

        categoryAdapter = new CategoryAdapter(this,categories);





        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        binding.catogariesList.setLayoutManager(layoutManager);
    }
}