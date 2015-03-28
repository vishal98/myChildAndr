package com.mychild.model;

import java.io.Serializable;

/**
 * Created by Sandeep on 21-03-2015.
 */
public class StudentDTO implements Serializable {

    private String stundentName = "";
    private int studentId = 0;

    public String getStundentName() {
        return stundentName;
    }

    public void setStundentName(String stundentName) {
        this.stundentName = stundentName;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }


}
