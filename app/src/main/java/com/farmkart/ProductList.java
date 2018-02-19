package com.farmkart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.farmkart.Interface.ItemClickListener;
import com.farmkart.ViewHolder.MenuViewHolder;
import com.farmkart.ViewHolder.ProductViewHolder;
import com.farmkart.models.Category;
import com.farmkart.models.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductList extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference productList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    String categoryId = "";

    FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter;

    //Search Functionality
    FirebaseRecyclerAdapter<Product, ProductViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        //Init Database
        database = FirebaseDatabase.getInstance();
        productList = database.getReference("Products");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        //get intent
        if(getIntent() != null){
            categoryId = getIntent().getStringExtra("categoryId");
            Log.d("TAGKUMAR1", categoryId);
            if(categoryId != null && !categoryId.isEmpty()){
                loadProductList(categoryId);
            }
        }

        materialSearchBar = (MaterialSearchBar) findViewById(R.id.search_bar);
        materialSearchBar.setHint("Search product name");
        loadSuggest(); // write function to load suggestion from firebase
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // When user type their text, we will change suggest list

                List<String> suggest = new ArrayList<>();
                for(String search: suggestList){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())){
                        suggest.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                // When search bar is closed
                // restore origin suggest adapter
                if(!enabled){
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                // When search finshed
                // Show result of search adapter
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(
                Product.class,
                R.layout.product_item,
                ProductViewHolder.class,
                productList.orderByChild("Name").equalTo(text.toString()) // Compare name

        ) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {
                viewHolder.txtProductName.setText(model.getName());
                viewHolder.txtProductPrice.setText(model.getPrice());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.imageView);
                final Product clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent productDetail = new Intent(ProductList.this, ProductDetail.class);
                        productDetail.putExtra("productId", searchAdapter.getRef(position).getKey());
                        startActivity(productDetail);
                    }
                });
            }
        };
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadSuggest() {
        productList.orderByChild("MenuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                            Product item = postSnapshot.getValue(Product.class);
                            suggestList.add(item.getName()); //Add name of product to suggest list
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void loadProductList(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>
                (Product.class, R.layout.product_item, ProductViewHolder.class,
                          productList.orderByChild("MenuId").equalTo(categoryId)
                ) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {
                viewHolder.txtProductName.setText(model.getName());
                viewHolder.txtProductPrice.setText(model.getPrice());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.imageView);
                final Product clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent productDetail = new Intent(ProductList.this, ProductDetail.class);
                        productDetail.putExtra("productId", adapter.getRef(position).getKey());
                        startActivity(productDetail);
                    }
                });
            }
        };
        Log.d("TAGKUMAR", "" + adapter.getItemCount());
        recyclerView.setAdapter(adapter);
    }
}
