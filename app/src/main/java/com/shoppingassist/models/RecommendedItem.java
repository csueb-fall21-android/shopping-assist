package com.shoppingassist.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.shoppingassist.models.Location;

@ParseClassName("RecommendedItem")
public class RecommendedItem   extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_NAME = "name";
    public static final String KEY_PRICE = "price";
    public static final String KEY_DETAILS = "details";
    public static final String KEY_BRAND = "brand";
    public static final String KEY_EXTERNALLINK = "externalLink";
    public static final String KEY_PICTUREFILE = "pictureFile";

    public ParseUser getUser() { return getParseUser(KEY_USER); }
    public void setUser(ParseUser user){ put(KEY_USER, user); }

    public String getName(){
        return getString(KEY_NAME);
    }
    public void setName(String name){
        put(KEY_NAME, name);
    }

    public String getPrice(){
        return getString(KEY_PRICE);
    }
    public void setPrice(Number price){
        put(KEY_PRICE, price);
    }
    public String getDetails(){
        return getString(KEY_DETAILS);
    }
    public void setDetails(String details){
        put(KEY_DETAILS, details);
    }
    public String getBrand(){
        return getString(KEY_BRAND);
    }
    public void setBrand(String brand){
        put(KEY_BRAND, brand);
    }
    public String getExternalLink(){
        return getString(KEY_EXTERNALLINK);
    }
    public void setExternalLink(String externalLink){
        put(KEY_EXTERNALLINK, externalLink);
    }

    public ParseFile getPictureFile(){ return getParseFile(KEY_PICTUREFILE); }
    public void setPictureFile(ParseFile parseFile){ put(KEY_PICTUREFILE, parseFile); }

    /**Location which is pointer to Location**/
    public String getLocation(){ return getString(KEY_LOCATION); }
    public void setLocation(Location location){ put(KEY_LOCATION, location); }

}
