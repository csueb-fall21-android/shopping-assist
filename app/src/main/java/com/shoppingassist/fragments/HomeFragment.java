package com.shoppingassist.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.shoppingassist.models.Item;
import com.shoppingassist.MainActivity;
import com.shoppingassist.R;
import com.shoppingassist.models.RecommendedItem;
import com.shoppingassist.adapters.SavedItemsDetailsAdapter;
import com.shoppingassist.adapters.SavedRecommendedItemsAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment";
    private RecyclerView rvItems;
    protected SavedItemsDetailsAdapter itemsAdapter;
    protected List<Item> allItems;
    protected SwipeRefreshLayout swipeContainer;
    protected List<RecommendedItem> savedItems;
    protected SavedRecommendedItemsAdapter savedItemsAdapter;
    protected RecyclerView rvSavedItems;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvItems = view.findViewById(R.id.rvItems);

        allItems = new ArrayList<>();
        itemsAdapter = new SavedItemsDetailsAdapter(getContext(), allItems, (MainActivity) getActivity());
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        queryItems();


//        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer); //Add back later when switch relative layout to swipeContainer
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                queryItems();
//            }
//        });

        rvSavedItems = view.findViewById(R.id.rvRecommendedItems);
        savedItems = new ArrayList<>();
        savedItemsAdapter = new SavedRecommendedItemsAdapter(getContext(), savedItems, (MainActivity) getActivity());
        rvSavedItems.setAdapter(savedItemsAdapter);
        rvSavedItems.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        querySavedItems();

    }

    protected void queryItems() { //Takes items we have and hands it over to the adapter
        //Specify which class to query
        //Toast.makeText(getActivity(), "Made it here", Toast.LENGTH_SHORT).show();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        //query.include(Item.KEY_PICTUREFILE);
        query.include(Item.KEY_USER);
        query.include(Item.KEY_LOCATION);
        //query.include(Item.KEY_NAME);
        query.setLimit(10); //Change later to reflect more pictures, picked 7 for testing
        query.addDescendingOrder(Item.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Item>() {
            @Override
            public void done(List<Item> items, ParseException e) { //Gets all items from database
                if (e != null){
                    //Toast.makeText(getActivity(), "Made it here", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Issue with getting items", e);
                    return;
                }
                for (Item item : items){
                    //Toast.makeText(getActivity(), "Made it here", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Item: " + item.getName());
                }
                    itemsAdapter.clear();
                    itemsAdapter.addAll(items);
//                    swipeContainer.setRefreshing(false);
            }
        });
    }

    /**
     * Query for Saved Recommended Items
     */
    protected void querySavedItems() {
        ParseQuery<RecommendedItem> query = ParseQuery.getQuery(RecommendedItem.class);
        query.include(Item.KEY_USER);

        query.setLimit(5);
        query.addDescendingOrder(Item.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<RecommendedItem>() {
            @Override
            public void done(List<RecommendedItem> recItems, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting saved items", e);
                    return;
                }
                for (RecommendedItem recItem : recItems){
                    Log.i(TAG, "Saved Item: " + recItem.getName());
                }
                savedItemsAdapter.clear();
                savedItemsAdapter.addAll(recItems);
            }
        });
    }
}