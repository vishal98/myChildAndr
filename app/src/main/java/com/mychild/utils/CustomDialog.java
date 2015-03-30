package com.mychild.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.mychild.adapters.ExamsTypesListviewAdapter;
import com.mychild.model.ExamModel;
import com.mychild.view.R;

import java.util.ArrayList;

/**
 * Created by Sandeep on 30-03-2015.
 */
public class CustomDialog {
    public static Dialog getExamsDialog(Context cntx, ArrayList<ExamModel> list, int examId) {
        final Dialog dialog = new Dialog(cntx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exams);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //cancelBtn.setOnClickListener();
        ListView examsListview = (ListView) dialog.findViewById(R.id.exams_types_lv);
        ExamsTypesListviewAdapter examsTypesListviewAdapter = new ExamsTypesListviewAdapter(cntx, R.layout.exam_type_listview_item, list, examId);
        examsListview.setAdapter(examsTypesListviewAdapter);
        dialog.show();
        return dialog;
    }
}
