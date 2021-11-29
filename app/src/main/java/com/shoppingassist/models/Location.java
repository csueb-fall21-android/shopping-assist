package com.shoppingassist.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Location")
public class Location extends ParseObject {
    public static final String KEY_LOCATIONID = "locationId";
    public static final String KEY_DESCRIPTOR = "descriptor";
    public static final String KEY_COORDINATES = "coordinates";

    //Added by Hector
    public static final String KEY_ADDRESS = "address";
    //public static final String KEY_USER = "user";

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

    //Added by Hector
    public String getAddress() { return getString(KEY_ADDRESS); }
    public void setAddress(String address) { put(KEY_ADDRESS, address); }

    //public String getKeyUser(){ return getString(KEY_USER);}
    //public void setKeyUser(String user){ put(KEY_USER, user);}
}
