package com.example.notesapp.Models;

import android.widget.TextView;

public class Notes {

    private int ID;
    private int parentID;
    private TextView content;

    private String name;
    private String photo;


    public Notes(TextView preset, int id, int parentId){
        content = preset;
        ID = id;
        parentID = parentId;
    }

    public void highlightText(String text) {

    }

    public Notes() {}

    public Notes(String name, String photo, int ID) {
        this.name = name;
        this.photo = photo;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() { return photo;}

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getID() { return ID;}

    public void setID(String ID) {
        this.photo = ID;
    }




}
