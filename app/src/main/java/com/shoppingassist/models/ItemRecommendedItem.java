package com.shoppingassist.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.shoppingassist.models.Item;
import com.shoppingassist.models.RecommendedItem;

@ParseClassName("ItemRecommendedItem")
public class ItemRecommendedItem  extends ParseObject {
    public static final String KEY_ITEM = "item";
    public static final String KEY_RECOMMENDED_ITEM = "recommendedItem";

    /**item which is pointer to Item**/
    public String getItem(){ return getString(KEY_ITEM); }
    public void setItem(Item item){ put(KEY_ITEM, item); }

    /**recommendedItem which is pointer to RecommendedItem**/
    public String getRecommendedItem(){ return getString(KEY_RECOMMENDED_ITEM); }
    public void setRecommendedItem(RecommendedItem recommendedItem){ put(KEY_RECOMMENDED_ITEM, recommendedItem); }
}