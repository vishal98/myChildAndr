package com.mychild.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sandeep on 26-03-2015.
 */
public class TeacherModel implements Serializable {
    private int teacherId;
    private String teacherName = "";
    private String teacherEmail = "";

    public ArrayList<GradeModel> getGradeModels() {
        return gradeModels;
    }

    public void setGradeModels(ArrayList<GradeModel> gradeModels) {
        this.gradeModels = gradeModels;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    private ArrayList<GradeModel> gradeModels;

}
