package com.example.notesapp;

import java.util.ArrayList;

public class Folders {
    private int ID;
    private int parentID;
    private ArrayList<Integer> children = new ArrayList<Integer>();

    public Folders(int id, int parentId){
        ID= id;
        parentID = parentId;
    }

    public void addChildren(int childId){
        children.add(childId);
    }

}
