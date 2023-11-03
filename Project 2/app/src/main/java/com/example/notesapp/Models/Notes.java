package com.example.notesapp.Models;

import android.widget.TextView;

public class Notes {

    private int ID;
    private int parentID;
    private TextView content;


    public Notes(TextView preset, int id, int parentId){
        content = preset;
        ID = id;
        parentID = parentId;
    }

    public void highlightText(String text) {

    }



}
