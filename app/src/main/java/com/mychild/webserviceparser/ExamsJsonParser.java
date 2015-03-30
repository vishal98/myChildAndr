package com.mychild.webserviceparser;

import com.mychild.model.ExamModel;
import com.mychild.model.ExamScheduleModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sandeep on 29-03-2015.
 */
public class ExamsJsonParser {
    public static ExamsJsonParser examsJsonParser = null;

    private ExamsJsonParser() {

    }

    public static ExamsJsonParser getInstance() {
        if (examsJsonParser == null) {
            examsJsonParser = new ExamsJsonParser();
        }
        return examsJsonParser;
    }

    public ArrayList<ExamModel> getExamsList(String str) {
        ArrayList<ExamModel> examsList = new ArrayList<ExamModel>();
        try {
            JSONObject jsonObject = new JSONObject(str);
            if (jsonObject.has("exams")) {
                JSONArray examsArray = jsonObject.getJSONArray("exams");
                int examsScheduleSize = examsArray.length();
                for (int i = 0; i < examsScheduleSize; i++) {
                    JSONObject examObj = examsArray.getJSONObject(i);
                    ExamModel examModel = new ExamModel();
                    if (examObj.has("examId")) {
                        examModel.setExamId(examObj.getString("examId"));
                    }
                    if (examObj.has("examType")) {
                        examModel.setExamType(examObj.getString("examType"));
                    }
                    if (examObj.has("schedule")) {
                        JSONArray examScheduleArray = examObj.getJSONArray("schedule");
                        ArrayList<ExamScheduleModel> examsScheduleList = new ArrayList<ExamScheduleModel>();
                        int subjectsSize = examScheduleArray.length();
                        for (int j = 0; j < subjectsSize; j++) {
                            JSONObject subjectObj = examScheduleArray.getJSONObject(j);
                            ExamScheduleModel examScheduleModel = new ExamScheduleModel();
                            if (subjectObj.has("subjectName")) {
                                examScheduleModel.setSubjectName(subjectObj.getString("subjectName"));
                            }
                            if (subjectObj.has("subjectSyllabus")) {
                                examScheduleModel.setSubjectSyllabus(subjectObj.getString("subjectSyllabus"));
                            }
                            if (subjectObj.has("teacherName")) {
                                examScheduleModel.setTeacherName(subjectObj.getString("teacherName"));
                            }
                            if (subjectObj.has("examStartTime")) {
                                examScheduleModel.setExamsStartTime(subjectObj.getString("examStartTime"));
                            }
                            if (subjectObj.has("examEndTime")) {
                                examScheduleModel.setExamsEndTime(subjectObj.getString("examEndTime"));
                            }
                            examsScheduleList.add(examScheduleModel);
                            examScheduleModel = null;
                        }
                        examModel.setExamScheduleList(examsScheduleList);
                        examsScheduleList = null;
                    }
                    examsList.add(examModel);
                }
            }
            examsList.add(dummyData());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return examsList;
    }

    private ExamModel dummyData() {
        ExamModel examModel = new ExamModel();
        examModel.setExamId("2");
        examModel.setExamType("Anual exams");
        ArrayList<ExamScheduleModel> list = new ArrayList<ExamScheduleModel>();
        ExamScheduleModel model1 = new ExamScheduleModel();
        model1.setExamsEndTime("Tuesday, 11 February 2014, 05:30:00 AM");
        model1.setExamsStartTime("Tuesday, 11 February 2014, 05:30:00 AM");
        model1.setTeacherName("AAAAAA");
        model1.setSubjectName("MATHS");
        list.add(model1);
        ExamScheduleModel model2 = new ExamScheduleModel();
        model2.setExamsEndTime("Tuesday, 11 February 2014, 05:30:00 AM");
        model2.setExamsStartTime("Tuesday, 11 February 2014, 05:30:00 AM");
        model2.setTeacherName("AAAAAA");
        model2.setSubjectName("SOCIAL");
        list.add(model2);
        examModel.setExamScheduleList(list);
        return examModel;
    }
}
