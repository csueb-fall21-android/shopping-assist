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
import com.shoppingassist.models.Item;

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
        View view = LayoutInflater.from(context).inflate(R.layout.rv_home_item, parent, false);
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
        private ImageView ivRImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRImage = itemView.findViewById(R.id.ivItemImage);
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
