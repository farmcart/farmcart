package com.farmkart;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.farmkart.Database.Database;
import com.farmkart.models.Order;
import com.farmkart.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ProductDetail extends AppCompatActivity {

    TextView product_name, product_price, product_description;
    ImageView product_image;

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String productId = "";

    FirebaseDatabase database;
    DatabaseReference products;

    Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //Firebase
        database = FirebaseDatabase.getInstance();
        products = database.getReference("Products");

        //Init View
        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(
                        productId,
                        currentProduct.getName(),
                        numberButton.getNumber(),
                        currentProduct.getPrice(),
                        currentProduct.getDiscount()
                ));

                Toast.makeText(ProductDetail.this, "Item added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        product_name = (TextView) findViewById(R.id.product_name);
        product_price = (TextView) findViewById(R.id.product_price);
        product_description = (TextView) findViewById(R.id.product_description);
        product_image = (ImageView) findViewById(R.id.img_product);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //Get ProductId from Intent
        if(getIntent() != null){
            productId = getIntent().getStringExtra("productId");
            if(productId != null && !productId.isEmpty()){
                getProductDetail(productId);
            }
        }

    }

    private void getProductDetail(final String productId) {
        products.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentProduct = dataSnapshot.getValue(Product.class);

                Picasso.with(getBaseContext()).load(currentProduct.getImage())
                        .into(product_image);
                collapsingToolbarLayout.setTitle(currentProduct.getName() );
                product_price.setText(currentProduct.getPrice());
                product_name.setText(currentProduct.getName());
                product_description.setText(currentProduct.getDescription());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
