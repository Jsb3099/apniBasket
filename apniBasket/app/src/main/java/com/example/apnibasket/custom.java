package com.example.apnibasket;

import android.graphics.Bitmap;

public class custom {
    private Bitmap imgid;
    private String movien , movier;
    public custom(Bitmap imgi , String name, String rating){
        imgid = imgi;
        movien = name;
        movier = rating;
    }

    public Bitmap getImgid() {
        return imgid;
    }

    public String getMovien() {
        return movien;
    }

    public String getMovier() {
        return movier;
    }
}
