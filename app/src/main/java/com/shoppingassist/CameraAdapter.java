package com.shoppingassist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import java.util.List;

public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.ViewHolder> {
    private Context context;
    private List<Item> items;

    public CameraAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CameraAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CameraAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRecent;
        private ImageView ivRImage;
        private TextView tvNearby;
        private ImageView ivNImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //tvRecent = itemView.findViewById(R.id.recentID);
            ivRImage = itemView.findViewById(R.id.rvFeedImage);
            //tvNearby = itemView.findViewById(R.id.nearbyID);
            ivNImage = itemView.findViewById(R.id.rvFeedImage); //If an issue make a new xml row item for this recycler view
        }

        public void bind(Item item){
            //Bind the item data to the view elements
            ParseFile image = item.getPictureFile();
            if(image != null){ //Add condition to either load recent image or nearby image
                Glide.with(context).load(item.getPictureFile().getUrl()).into(ivRImage);
            }
        }
    }

    //Clean all elements of the recycler view
    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    //Add a list of items
    public void addAll(List<Item> list){
        items.addAll(list);
        notifyDataSetChanged();
    }
}
