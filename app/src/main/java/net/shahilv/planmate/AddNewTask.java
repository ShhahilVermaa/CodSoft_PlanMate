package net.shahilv.planmate;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import net.shahilv.planmate.Model.ToDoModel;
import net.shahilv.planmate.Utils.DataBaseHelper;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewTask";

    private EditText mEditText;
    private Button mSaveButton;

    private DataBaseHelper myDB;
    private boolean isUpdate = false;
    private Bundle bundle;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_new_task,container,false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEditText = view.findViewById(R.id.editText);
        mSaveButton = view.findViewById(R.id.addButton);

        myDB = new DataBaseHelper(getActivity());
        boolean isUpdate;

        bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String task = bundle.getString("Task");
            mEditText.setText(task);

            if(task.length() > 0){
                mSaveButton.setEnabled(false);
            }
        } else {
            isUpdate = false;
        }
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    if(charSequence.toString().trim().isEmpty()){
                        mSaveButton.setEnabled(false);
                        mSaveButton.setBackgroundColor(Color.GRAY);
                    }else{
                        mSaveButton.setEnabled(true);
                    }
             }
        });
       mSaveButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String text = mEditText.getText().toString().trim();
               if(isUpdate && bundle != null){
                    myDB.updateTask(bundle.getInt("Id"),text);
               }else{
                   ToDoModel item = new ToDoModel();
                   item.setTask(text);
                   item.setStatus(0);
                   myDB.insertTask(item);
               }
               dismiss();
           }
       });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}
