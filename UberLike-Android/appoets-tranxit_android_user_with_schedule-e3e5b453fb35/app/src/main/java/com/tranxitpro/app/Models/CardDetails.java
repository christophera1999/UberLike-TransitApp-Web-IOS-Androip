package com.tranxitpro.app.Models;

import java.io.Serializable;

/**
 * Created by admin on 8/4/2017.
 */

public class CardDetails implements Serializable {
    public String id;
    public String user_id;
    public String last_four;
    public String card_id;
    public String brand;
    public String is_default;
    private String selected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLast_four() {
        return last_four;
    }

    public void setLast_four(String last_four) {
        this.last_four = last_four;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getIs_default() {
        return is_default;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
