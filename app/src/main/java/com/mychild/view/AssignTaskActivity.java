package com.mychild.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.adapters.CustomAdapter;
import com.mychild.adapters.SubjectSpinnerAdapter;
import com.mychild.model.GradeModel;
import com.mychild.model.StudentDTO;
import com.mychild.model.SubjectModel;
import com.mychild.model.TeacherModel;
import com.mychild.threads.HttpConnectThread;
import com.mychild.utils.AsyncTaskInterface;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.TopBar;
import com.mychild.webserviceparser.TeacherHomeJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class AssignTaskActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, RequestCompletion, AsyncTaskInterface, AdapterView.OnItemSelectedListener {

    private TopBar topBar;
    private Spinner classSpinner, subjectSpinner;
    private RadioGroup selectStudioRG;
    private Button assignTaskBtn;
    private TextView chooseDateTV;
    private EditText taskET;
    private final int REQUEST_CODE = 1234;
    private boolean updateCheckStatus = false;
    String teacherName = "/test_teacher";
    private String base_url = "http://Default-Environment-8tpprium54.elasticbeanstalk.com/Teacher/id";
    private String base_and_post_url = "http://Default-Environment-8tpprium54.elasticbeanstalk.com/app/teacher/homework/save";
    private String post_url = "/app/teacher/homework/save";
    private String subject_base_url = "http://Default-Environment-8tpprium54.elasticbeanstalk.com";
    private TeacherModel teacherModel = null;
    private int selectedGrade = 0, SelectedSubject = 0;
    private CustomAdapter classNamesAdapter = null;
    private SubjectSpinnerAdapter subjectSpinnerAdapter = null;
    private Calendar calendar = Calendar.getInstance();
    private ArrayList<StudentDTO> selectedStudents = null;

    enum RequestType {
        TYPE_TEACHER_DETAILS, TYPE_SUBJECTS, TYPE_POST_DATA;
    }

    ;
    RequestType type = RequestType.TYPE_TEACHER_DETAILS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task);
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.titleTV.setText(getString(R.string.assign_task_title));
        classSpinner = (Spinner) findViewById(R.id.class_spinner);
        classSpinner.setOnItemSelectedListener(this);
        subjectSpinner = (Spinner) findViewById(R.id.subject_spinner);
        subjectSpinner.setOnItemSelectedListener(this);
        selectStudioRG = (RadioGroup) findViewById(R.id.select_students_rg);
        assignTaskBtn = (Button) findViewById(R.id.assign_task_btn);
        taskET = (EditText) findViewById(R.id.task_et);
        chooseDateTV = (TextView) findViewById(R.id.choose_date_tv);
        chooseDateTV.setOnClickListener(this);
        if (CommonUtils.isNetworkAvailable(this)) {
            RequestType type = RequestType.TYPE_TEACHER_DETAILS;
            httpConnectThread = new HttpConnectThread(this, null, this);
            httpConnectThread.execute(base_url + teacherName);
           /* Constants.showProgress(this);
            WebServiceCall call = new WebServiceCall(AssignTaskActivity.this);
          //  call.getCallRequest(getString(R.string.base_url) + getString(R.string.url_teacher_deatils) + teacherName);
            call.getCallRequest(test_url);
            CommonUtils.getLogs("URL is : " + getString(R.string.base_url) + getString(R.string.url_teacher_deatils) + teacherName);*/
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
        //register with listeners
        assignTaskBtn.setOnClickListener(this);
        topBar.backArrowIV.setOnClickListener(this);
        selectStudioRG.setOnCheckedChangeListener(this);

    }

    private void setClassSpinner() {
        classNamesAdapter = new CustomAdapter(this, R.layout.drop_down_item, teacherModel.getGradeModels());
        classSpinner.setAdapter(classNamesAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_arrow_iv:
                onBackPressed();
                break;
            case R.id.choose_date_tv:
                showDatePicker();
                break;
            case R.id.assign_task_btn:
                postAssignTaskData();
                break;
            default:
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case -1:
                updateCheckStatus = false;
                break;
            case R.id.select_all_rb:
                break;
            case R.id.select_few_rb:
                if (!updateCheckStatus) {
                    Intent intent = new Intent(this, SelectStudentActivity.class);
                    intent.putExtra(getString(R.string.students_data), teacherModel.getGradeModels().get(selectedGrade).getStudentsModels());
                    startActivityForResult(intent, REQUEST_CODE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && data != null) {
            updateCheckStatus = false;
            selectedStudents = (ArrayList<StudentDTO>) data.getExtras().getSerializable(getString(R.string.students_data));
        } else {
            CommonUtils.getToastMessage(this, "Nothing Selected");
            selectedStudents.clear();
            selectedStudents = null;
            updateCheckStatus = true;
            selectStudioRG.clearCheck();
        }
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        CommonUtils.getLogs("Response::::" + responseJson);
        CommonUtils.getLogs("Response::::" + responseArray);
        Constants.stopProgress(this);
    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("Response::::" + error);
        Constants.stopProgress(this);
    }

    private void showDatePicker() {
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        final DatePickerDialog dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            boolean fired = false;

            public void onDateSet(final DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                CommonUtils.getLogs("Double Fired::" + year + ":" + monthOfYear + ":" + dayOfMonth);
                int month = monthOfYear + 1;
                chooseDateTV.setText(dayOfMonth + "-" + month + "-" + year);
                calendar.set(year, monthOfYear, dayOfMonth);
                if (fired == true) {
                    CommonUtils.getLogs("Double fire occured. Silently-ish returning");
                    return;
                } else {
                    //first time fired
                    fired = true;
                }
                //Normal date picking logic goes here
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dateDialog.show();
    }

    private void callSubjectsWebService(String classSection) {
        type = RequestType.TYPE_SUBJECTS;
        String postUrl = "/app/subject/" + classSection;
        if (CommonUtils.isNetworkAvailable(this)) {
            httpConnectThread = new HttpConnectThread(this, null, this);
            httpConnectThread.execute(subject_base_url + postUrl);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

    private void setSubjectAdapter(ArrayList<SubjectModel> list) {
        subjectSpinnerAdapter = new SubjectSpinnerAdapter(this, R.layout.drop_down_item, list);
        subjectSpinner.setAdapter(subjectSpinnerAdapter);
    }

    @Override
    public void setAsyncTaskCompletionListener(String object) {
        CommonUtils.getLogs("Response:::" + object);
        switch (type) {
            case TYPE_TEACHER_DETAILS:
                try {
                    JSONObject obj = new JSONObject(object);
                    teacherModel = TeacherHomeJsonParser.getInstance().getTeacherDetails(obj);
                    setClassSpinner();
                    obj = null;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case TYPE_SUBJECTS:
                TeacherModel teacherModel = TeacherHomeJsonParser.getInstance().getSubjectsList(object);
                this.teacherModel.setSubjectsList(teacherModel.getSubjectsList());
                setSubjectAdapter(teacherModel.getSubjectsList());
                break;
            case TYPE_POST_DATA:
                break;
            default:
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int spinnserID = parent.getId();
        switch (spinnserID) {
            case R.id.class_spinner:
                selectedGrade = position;
                TextView tv = (TextView) view.findViewById(R.id.drop_down_item);
                GradeModel gradeModel = (GradeModel) tv.getTag();
                callSubjectsWebService(gradeModel.getGradeName() + "/" + gradeModel.getSection());
                break;
            case R.id.subject_spinner:
                SelectedSubject = position;
                break;
            default:
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void postAssignTaskData() {
        if (CommonUtils.isNetworkAvailable(this)) {
            if (classNamesAdapter == null || classNamesAdapter.getCount() <= 0) {
                CommonUtils.getToastMessage(this, "Please Select Class");
                return;
            } else if (subjectSpinnerAdapter == null || subjectSpinnerAdapter.getCount() <= 0) {
                CommonUtils.getToastMessage(this, "Please Select Subject");
                return;
            } else {
                String classText = classSpinner.getSelectedItem().toString();
                String subjectText = subjectSpinner.getSelectedItem().toString();
                CommonUtils.getLogs("Subject:::" + subjectText);
                CommonUtils.getLogs("SSSSS:" + subjectSpinner);
                String messageText = taskET.getText().toString();
                String dueDateText = chooseDateTV.getText().toString();
               /* if(classText.equals("")){
                    CommonUtils.getToastMessage(this, "Please Select Class");
                }else if(subjectText.equals("")){
                    CommonUtils.getToastMessage(this, "Please Select Subject");
                }else */
                if (messageText.equals("")) {
                    CommonUtils.getToastMessage(this, "Please Enter message");
                } else if (dueDateText.equals("")) {
                    CommonUtils.getToastMessage(this, "Please Select Due Date");
                } else if (selectStudioRG.getCheckedRadioButtonId() == -1) {
                    CommonUtils.getToastMessage(this, "Please Select Students");
                } else {
                    type = RequestType.TYPE_POST_DATA;
                    JSONObject jsonObject = new JSONObject();
                    Constants.showProgress(AssignTaskActivity.this);
                    try {
                        if (selectStudioRG.getCheckedRadioButtonId() == R.id.select_all_rb) {
                            jsonObject.put("gradeFlag", "g");
                        } else {
                            jsonObject.put("gradeFlag", "s");
                            JSONArray array = new JSONArray();
                            for (StudentDTO dto : selectedStudents) {
                                array.put(dto.getStudentId() + "");
                            }
                            jsonObject.put("studentsList", array);
                        }
                        jsonObject.put("grade", teacherModel.getGradeModels().get(selectedGrade).getGradeName());
                        jsonObject.put("section", teacherModel.getGradeModels().get(selectedGrade).getSection());
                        jsonObject.put("subject", teacherModel.getSubjectsList().get(SelectedSubject).getSubjectName());
                        jsonObject.put("homework", teacherModel.getSubjectsList().get(SelectedSubject).getSubjectName() + " Homework");
                        jsonObject.put("duedata", dueDateText);
                        jsonObject.put("message", messageText);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    CommonUtils.getLogs("POST data::" + jsonObject);
                    WebServiceCall webServiceCall = new WebServiceCall(this);
                    webServiceCall.postToServer(jsonObject, base_and_post_url);
            /*httpConnectThread = new HttpConnectThread(this, jsonObject, this);
            httpConnectThread.execute(getString(R.string.base_url) + post_url);*/
           /* Constants.showProgress(this);
            WebServiceCall call = new WebServiceCall(AssignTaskActivity.this);
          //  call.getCallRequest(getString(R.string.base_url) + getString(R.string.url_teacher_deatils) + teacherName);
            call.getCallRequest(test_url);
            CommonUtils.getLogs("URL is : " + getString(R.string.base_url) + getString(R.string.url_teacher_deatils) + teacherName);*/
                }
            }
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }
}
