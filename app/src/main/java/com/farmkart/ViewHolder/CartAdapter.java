package com.farmkart.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.farmkart.Interface.ItemClickListener;
import com.farmkart.R;
import com.farmkart.models.Order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by kumar on 2/10/2018.
 */


class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtCartName, txtCartPrice;
    public ImageView imgCartCount;

    private ItemClickListener itemClickListener;

    public void setTxtCartName(TextView txtCartName) {
        this.txtCartName = txtCartName;
    }

    public CartViewHolder(View itemView) {
        super(itemView);
        txtCartName = (TextView) itemView.findViewById(R.id.cart_item_name);
        txtCartPrice = (TextView) itemView.findViewById(R.id.cart_item_price);
        imgCartCount = (ImageView) itemView.findViewById(R.id.cart_item_count);
    }

    @Override
    public void onClick(View view) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

    private List<Order> listData = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+listData.get(position).getQuantity(), Color.RED);
        holder.imgCartCount.setImageDrawable(drawable);

        Locale locale = new Locale("en", "IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice()))
                *(Integer.parseInt(listData.get(position).getQuantity()));
        holder.txtCartPrice.setText(fmt.format(price));
        holder.txtCartName.setText(listData.get(position).getProductName());
        Log.d("TAGNAME", holder.txtCartName.getText().toString());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
