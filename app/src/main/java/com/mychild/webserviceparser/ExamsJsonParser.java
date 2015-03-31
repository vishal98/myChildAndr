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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return examsList;
    }
}
