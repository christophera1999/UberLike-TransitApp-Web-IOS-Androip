package com.tranxitpro.app.Models;


import java.io.Serializable;

/**
 * Created by Kyra on 1/11/2016.
 */
public class PlaceAutoComplete implements Serializable {

    private String place_id;
    private String description;

    public String getPlaceDesc() {
        return description;
    }

    public void setPlaceDesc(String placeDesc) {
        description = placeDesc;
    }

    public String getPlaceID() {
        return place_id;
    }

    public void setPlaceID(String placeID) {
        place_id = placeID;
    }

}
