package com.shoppingassist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.shoppingassist.R;
import com.shoppingassist.interfaces.OnSavedListItemInteractionListener;
import com.shoppingassist.models.RecommendedItem;

import java.util.List;

public class SavedRecommendedItemsAdapter extends RecyclerView.Adapter<SavedRecommendedItemsAdapter.ViewHolder> {

    private Context context;
    private List<RecommendedItem> recommendedItems;
    private final OnSavedListItemInteractionListener mListener;

    public SavedRecommendedItemsAdapter(Context context, List<RecommendedItem> recommendedItems, OnSavedListItemInteractionListener mListener) {
        this.mListener = mListener;
        this.context = context;
        this.recommendedItems = recommendedItems;
    }

    @NonNull
    @Override
    public SavedRecommendedItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_recommended_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedRecommendedItemsAdapter.ViewHolder holder, int position) {
        RecommendedItem recItem = recommendedItems.get(position);
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
        return recommendedItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivRecommendedImage);
        }

        public void bind(RecommendedItem recItem) {
            ParseFile image = recItem.getPictureFile();

            if (image != null) {
                Glide.with(context).load(recItem.getPictureFile().getUrl()).into(ivImage);
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        recommendedItems.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<RecommendedItem> list) {
        recommendedItems.addAll(list);
        notifyDataSetChanged();
    }
}
