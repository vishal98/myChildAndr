package com.mychild.model;

import java.util.ArrayList;

/**
 * Created by Sandeep on 29-03-2015.
 */
public class ExamModel {
    private String examId;
    private String examType;

    public ArrayList<ExamScheduleModel> getExamScheduleList() {
        return examScheduleList;
    }

    public void setExamScheduleList(ArrayList<ExamScheduleModel> examScheduleList) {
        this.examScheduleList = examScheduleList;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    private ArrayList<ExamScheduleModel> examScheduleList;

}
