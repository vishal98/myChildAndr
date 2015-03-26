package com.mychild.model;

import java.util.ArrayList;

/**
 * Created by Vijay on 3/25/15.
 */
public class ParentModel {

    public ArrayList<String> numberOfChild= new ArrayList<String>();

    public void setNumberOFChild(ArrayList<String> numberOfChild){
        this.numberOfChild = numberOfChild;

    }

    public ArrayList<String> getNumberOFChild(ArrayList<String> numberOfChild){
        return numberOfChild;
    }
}
