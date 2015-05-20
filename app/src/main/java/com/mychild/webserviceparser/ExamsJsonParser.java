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

    public ArrayList<ExamModel> getExamsList(JSONObject responseJson) {
        ArrayList<ExamModel> examsList = new ArrayList<ExamModel>();
        try {
            //JSONObject responseJson = new JSONObject(str);
            if (responseJson.has("exam")) {
                JSONArray examsArray = responseJson.getJSONArray("exam");
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
                    if (examObj.has("examSchedule")) {
                        JSONArray examScheduleArray = examObj.getJSONArray("examSchedule");
                        ArrayList<ExamScheduleModel> examsScheduleList = new ArrayList<ExamScheduleModel>();
                        int subjectsSize = examScheduleArray.length();
                        for (int j = 0; j < subjectsSize; j++) {
                            JSONObject subjectObj = examScheduleArray.getJSONObject(j);
                            ExamScheduleModel examScheduleModel = new ExamScheduleModel();

                            if (subjectObj.has("syllabus")) {

                                    JSONObject syllabusObj = subjectObj.getJSONObject("syllabus");
                                    if (syllabusObj.has("syllabus")) {
                                        examScheduleModel.setSubjectSyllabus(syllabusObj.getString("syllabus"));
                                    }
                            }

                            if (subjectObj.has("subject")) {

                                JSONObject syllabusSubjectObj = subjectObj.getJSONObject("subject");
                                if (syllabusSubjectObj.has("subjectName")) {
                                    examScheduleModel.setSubjectName(syllabusSubjectObj.getString("subjectName"));
                                }
                            }

//                            if (subjectObj.has("subjectName")) {
//                                examScheduleModel.setSubjectName(subjectObj.getString("subjectName"));
//                            }
//                            if (subjectObj.has("subjectSyllabus")) {
//                                examScheduleModel.setSubjectSyllabus(subjectObj.getString("subjectSyllabus"));
//                            }
//                            if (subjectObj.has("teacherName")) {
//                                examScheduleModel.setTeacherName(subjectObj.getString("teacherName"));
//                            }
                            if (subjectObj.has("startTime")) {
                                examScheduleModel.setExamsStartTime(subjectObj.getString("startTime"));
                            }
                            if (subjectObj.has("endTime")) {
                                examScheduleModel.setExamsEndTime(subjectObj.getString("endTime"));
                            }
                            if (subjectObj.has("examDate")) {
                                examScheduleModel.setExamsDate(subjectObj.getString("examDate"));
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
