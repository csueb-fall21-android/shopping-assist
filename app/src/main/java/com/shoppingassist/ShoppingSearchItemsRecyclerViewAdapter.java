package com.shoppingassist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shoppingassist.models.ShoppingItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ShoppingItem} and makes a call to the
 * specified {@link OnListItemInteractionListener}.
 */
public class ShoppingSearchItemsRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingSearchItemsRecyclerViewAdapter.ShoppingItemViewHolder> {

    private final List<ShoppingItem> items;
    private final OnListItemInteractionListener mListener;

    public ShoppingSearchItemsRecyclerViewAdapter(List<ShoppingItem> items, OnListItemInteractionListener listener) {
        this.items = items;
        mListener = listener;
    }

    @Override
    public ShoppingItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_search_item, parent, false);
        return new ShoppingItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShoppingItemViewHolder holder, int position) {
        ShoppingItem item = items.get(position);
        holder.mItem = item;
        holder.tvProdName.setText(item.title);
        holder.tvPrice.setText(item.price);
        holder.tvLocation.setText(item.source);
//        Glide.with(holder.mView)
//                .load(item.image)
//                .centerInside()
//                .into(holder.ivImage);

        holder.btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onItemClick(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ShoppingItemViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvProdName;
        public final ImageView ivImage;
        public final TextView tvPrice;
        public final TextView tvLocation;
        public final ImageButton btnLink;
        public ShoppingItem mItem;

        public ShoppingItemViewHolder(View view) {
            super(view);
            mView = view;
            tvProdName = (TextView) view.findViewById(R.id.rvProdName);
            ivImage = (ImageView) view.findViewById(R.id.rvImage);
            tvPrice = (TextView) view.findViewById(R.id.rvPrice);
            tvLocation = (TextView) view.findViewById(R.id.rvLocation);
            btnLink = (ImageButton) view.findViewById(R.id.rvButton);
        }

        @Override
        public String toString() {
            return tvProdName.toString() + " '" + tvPrice.getText() + "'";
        }
    }
}
