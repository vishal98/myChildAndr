package com.mychild.webserviceparser;

import android.util.Log;

import com.mychild.model.ChildDetailsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Vijay on 3/21/15.
 */
public class ParentHomeJsonParser  {
    public static ParentHomeJsonParser parentHomeJsonParser = null;

    private ParentHomeJsonParser() {

    }

    public static ParentHomeJsonParser getInstance() {
        if (parentHomeJsonParser == null) {
            parentHomeJsonParser = new ParentHomeJsonParser();
        }
        return parentHomeJsonParser;
    }
//    public ArrayList<ChildDetailsModel> getExamsList(String str) {
//        ArrayList<ChildDetailsModel> childList = new ArrayList<ChildDetailsModel>();
//        try {
//            JSONObject jsonObject = new JSONObject(str);
//            if (jsonObject.has("exams")) {
//                JSONArray examsArray = jsonObject.getJSONArray("exams");
//                int examsScheduleSize = examsArray.length();
//                for (int i = 0; i < examsScheduleSize; i++) {
//                    JSONObject examObj = examsArray.getJSONObject(i);
//                    ChildDetailsModel childModel = new ChildDetailsModel();
//                    if (examObj.has("examId")) {
//                        childModel.setExamId(examObj.getString("examId"));
//                    }
//                    if (examObj.has("examType")) {
//                        childModel.setExamType(examObj.getString("examType"));
//                    }
//                    if (examObj.has("schedule")) {
//                        JSONArray examScheduleArray = examObj.getJSONArray("schedule");
//                        ArrayList<ExamScheduleModel> examsScheduleList = new ArrayList<ExamScheduleModel>();
//                        int subjectsSize = examScheduleArray.length();
//                        for (int j = 0; j < subjectsSize; j++) {
//                            JSONObject subjectObj = examScheduleArray.getJSONObject(j);
//                            ExamScheduleModel examScheduleModel = new ExamScheduleModel();
//                            if (subjectObj.has("subjectName")) {
//                                examScheduleModel.setSubjectName(subjectObj.getString("subjectName"));
//                            }
//                            if (subjectObj.has("subjectSyllabus")) {
//                                examScheduleModel.setSubjectSyllabus(subjectObj.getString("subjectSyllabus"));
//                            }
//                            if (subjectObj.has("teacherName")) {
//                                examScheduleModel.setTeacherName(subjectObj.getString("teacherName"));
//                            }
//                            if (subjectObj.has("examStartTime")) {
//                                examScheduleModel.setExamsStartTime(subjectObj.getString("examStartTime"));
//                            }
//                            if (subjectObj.has("examEndTime")) {
//                                examScheduleModel.setExamsEndTime(subjectObj.getString("examEndTime"));
//                            }
//                            examsScheduleList.add(examScheduleModel);
//                            examScheduleModel = null;
//                        }
//                        childModel.setExamScheduleList(examsScheduleList);
//                        examsScheduleList = null;
//                    }
//                    childList.add(childModel);
//                }
//            }
//            childList.add(dummyData());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return examsList;
//    }

    public static ArrayList<ChildDetailsModel> getChildrenListwithID(JSONArray jsonArray) {
        ArrayList<ChildDetailsModel> childList = new ArrayList<ChildDetailsModel>();

        if (jsonArray != null) {
            int size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    JSONArray childrenArray = object.getJSONArray("children");

                    for(int j= 0; j<childrenArray.length();j++ ){

                        JSONObject noOfStudent = childrenArray.getJSONObject(j);
                        ChildDetailsModel childModel = new ChildDetailsModel();
                        if (noOfStudent.has("studentId")) {
                            childModel.setStudentId(noOfStudent.getString("studentId"));
                        }
                        if (noOfStudent.has("studentName")) {
                            childModel.setStundentName(noOfStudent.getString("studentName"));
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i("Childata", childList.toString());
        return childList;
    }

//    public static ArrayList<HashMap<String, String>> getChildrenListwithID(JSONArray jsonArray) {
//        ArrayList<HashMap<String, String>> childrenNameAndIdArraylist = null;
//        LinkedHashMap<String, String> childrenNameAndIdHashMap = null;
//        CustomDialogueAdapter customDialogueAdapter ;
//        if (jsonArray != null) {
//            int size = jsonArray.length();
//            for (int i = 0; i < size; i++) {
//                try {
//                    JSONObject object = jsonArray.getJSONObject(i);
//                    JSONArray childrenArray = object.getJSONArray("children");
//                    childrenNameAndIdArraylist = new ArrayList<HashMap<String, String>>();
//                    for(int j= 0; j<childrenArray.length();j++ ){
//                        childrenNameAndIdHashMap = new LinkedHashMap<String, String>();
//                        JSONObject noOfStudent = childrenArray.getJSONObject(j);
//                        if (noOfStudent.has("studentId")) {
//                            childrenNameAndIdHashMap.put("studentId", noOfStudent.getString("studentId"));
//                        }
//                        if (noOfStudent.has("studentName")) {
//                            childrenNameAndIdHashMap.put("studentName", noOfStudent.getString("studentName"));
//                        }
//                        childrenNameAndIdArraylist.add(childrenNameAndIdHashMap);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        //customDialogueAdapter = new CustomDialogueAdapter(activity,childrenNameAndIdArraylist);
//        Log.i("Childata", childrenNameAndIdArraylist.toString());
//        return childrenNameAndIdArraylist;
//    }




    public static ArrayList<HashMap<String, String>> getChildrenGradeAndSection(JSONArray jsonArray) {
        ArrayList<HashMap<String, String>> childrenGradeAndSection = null;
        LinkedHashMap<String, String> childrenGradeAndSectionMap = null;
       try {
        if (jsonArray != null) {
            int size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                if(object.has("children")){
                    JSONArray childrenArray = object.getJSONArray("children");
                    childrenGradeAndSection = new ArrayList<HashMap<String, String>>();
                    for(int j= 0; j<childrenArray.length();j++ ){
                        childrenGradeAndSectionMap = new LinkedHashMap<String, String>();

                        JSONObject childName = childrenArray.getJSONObject(j);
                        if (childName.has("studentName")) {
                            String studentName = childName.getString("studentName");
                            childrenGradeAndSectionMap.put("studentName",studentName);
                        }
                        if (childName.has("grade")) {
                            String grade = childName.getString("grade");
                            childrenGradeAndSectionMap.put("grade",grade);
                        }
                        if (childName.has("section")) {
                            String section = childName.getString("section");
                            childrenGradeAndSectionMap.put("section",section);
                        }
                       // Log.i("childrenGradeSectionMap", childrenGradeAndSectionMap.toString());
                        childrenGradeAndSection.add(childrenGradeAndSectionMap);
                       // Log.i("childrenGradeAndSection", childrenGradeAndSection.toString());
                    }
                }
            }
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("childrenGradeAndSection", childrenGradeAndSection.toString());
        return childrenGradeAndSection;
    }

}
