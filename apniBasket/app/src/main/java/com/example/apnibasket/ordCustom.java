package com.example.apnibasket;

public class ordCustom {
    private String items,price,numberid;
    public ordCustom(String it,String pr, String nid){
        items = it;
        price = pr;
        numberid = nid;
    }

    public String getItems() {
        return items;
    }

    public String getPrice() {
        return price;
    }

    public String getNumberid() {
        return numberid;
    }
}
