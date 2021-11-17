package com.shoppingassist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.shoppingassist.interfaces.OnSavedItemDetailsListener;

import java.util.List;

public class SavedItemsDetailsAdapter extends RecyclerView.Adapter<SavedItemsDetailsAdapter.ItemViewHolder> {

    private Context context;
    private List<Item> item;
    private final OnSavedItemDetailsListener mListener;

    public SavedItemsDetailsAdapter(Context context, List<Item> allItems, OnSavedItemDetailsListener mListener) {
        this.context = context;
        this.mListener = mListener;
        this.item = allItems;
    }

    @NonNull
    @Override
    public SavedItemsDetailsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_recommended_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedItemsDetailsAdapter.ItemViewHolder holder, int position) {
        Item recItem = item.get(position);
        holder.bind(recItem);

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onClick(recItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivRecommendedImage);
        }

        public void bind(Item recItem) {
            ParseFile image = recItem.getPictureFile();

            if (image != null) {
                Glide.with(context).load(recItem.getPictureFile().getUrl()).into(ivImage);
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        item.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Item> list) {
        item.addAll(list);
        notifyDataSetChanged();
    }
}
