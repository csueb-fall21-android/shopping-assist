package com.shoppingassist.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.shoppingassist.R;
import com.shoppingassist.interfaces.OnSavedItemDetailsListener;
import com.shoppingassist.models.Item;

import java.util.List;

public class SavedItemsDetailsAdapter extends RecyclerView.Adapter<SavedItemsDetailsAdapter.ItemViewHolder> {

    public static final String TAG = "SavedItemsDetailsAdapter";
    private Context context;
    private List<Item> items;
    private final OnSavedItemDetailsListener mListener;

    public SavedItemsDetailsAdapter(Context context, List<Item> allItems, OnSavedItemDetailsListener mListener) {
        this.context = context;
        this.mListener = mListener;
        this.items = allItems;
    }

    @NonNull
    @Override
    public SavedItemsDetailsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_home_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedItemsDetailsAdapter.ItemViewHolder holder, int position) {
        Item recItem = items.get(position);
        holder.bind(recItem);

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    Log.i(TAG, "Item clicked: " + recItem.getName());
                    mListener.onClick(recItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImage;
        private TextView tvItemName;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivItemImage);
            tvItemName = itemView.findViewById(R.id.tvItemName);
        }

        public void bind(Item recItem) {
            ParseFile image = recItem.getPictureFile();
            Log.i(TAG, "Item: " + recItem.getName());
            tvItemName.setText(recItem.getName());

            if (image != null) {
                Glide.with(context).load(recItem.getPictureFile().getUrl()).into(ivImage);
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Item> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }
}
