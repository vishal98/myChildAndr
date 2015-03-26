package com.mychild.webserviceparser;

import com.mychild.model.StudentDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sandeep on 21-03-2015.
 */
public class TeacherHomeJsonParser {
    public static TeacherHomeJsonParser teacherHomeJsonParser = null;

    private TeacherHomeJsonParser() {

    }

    public static TeacherHomeJsonParser getInstance() {
        if (teacherHomeJsonParser == null) {
            teacherHomeJsonParser = new TeacherHomeJsonParser();
        }
        return teacherHomeJsonParser;
    }
    public ArrayList<StudentDTO> getStudentsList(JSONArray jsonArray) {
        ArrayList<StudentDTO> studentList = new ArrayList<StudentDTO>();
        if (jsonArray != null) {
            int size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                try {
                    StudentDTO studentDTO = new StudentDTO();
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (object.has("studentName")) {
                        studentDTO.setStundentName(object.getString("studentName"));
                    }
                    if (object.has("studentId")) {
                        studentDTO.setStudentId(object.getInt("studentId"));
                    }
                    studentList.add(studentDTO);
                    studentDTO = null;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return studentList;
    }
}
