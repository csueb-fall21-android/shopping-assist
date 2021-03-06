package com.shoppingassist.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("AppLocation")
public class AppLocation extends ParseObject {
    public static final String KEY_LOCATIONID = "locationId";
    public static final String KEY_DESCRIPTOR = "descriptor";
    public static final String KEY_COORDINATES = "coordinates";

    public String getDescriptor(){
        return getString(KEY_DESCRIPTOR);
    }
    public void setDescriptor(String descriptor){
        put(KEY_DESCRIPTOR, descriptor);
    }

    public String getCoordinates(){
        return getString(KEY_COORDINATES);
    }
    public void setCoordinates(String coordinates){
        put(KEY_COORDINATES, coordinates);
    }
}
