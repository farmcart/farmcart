package com.farmkart.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.farmkart.Interface.ItemClickListener;
import com.farmkart.R;

/**
 * Created by kumar on 2/6/2018.
 */

public class ProductViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductPrice;
    public ImageView imageView;

    private ItemClickListener itemClickListener;

    public ProductViewHolder(View itemView) {
        super(itemView);

        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
        imageView = (ImageView) itemView.findViewById(R.id.product_image);

        itemView.setOnClickListener(this);
    }

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);

    }

}
