package net.shahilv.planmate.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.shahilv.planmate.AddNewTask;
import net.shahilv.planmate.MainActivity;
import net.shahilv.planmate.Model.ToDoModel;
import net.shahilv.planmate.R;
import net.shahilv.planmate.Utils.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ToDoaAdapter extends RecyclerView.Adapter<ToDoaAdapter.MyViewHolder>{
    private List<ToDoModel> mList;
    private MainActivity activity;
    private DataBaseHelper myDB;

    public ToDoaAdapter(DataBaseHelper myDB , MainActivity activity){
        this.activity = activity;
        this.myDB = myDB;
        this.mList = new ArrayList<>();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout , parent , false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = mList.get(position);
        holder.checkBox.setText(item.getTask());
        holder.checkBox.setChecked (toboolean(item.getStatus()));
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean isChecked) {
                if(compoundButton.isChecked()){
                        myDB.updateStatus(item.getId(),1);
                }else{
                    myDB.updateStatus(item.getId(),0);
                }
            }
        });
    }

    public boolean toboolean(int num){
        return num != 0;
    }

    public Context getContext(){
        return activity;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setTasks(List<ToDoModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position){
        if (position < 0 || position >= mList.size()) {
            Log.e("ToDoAdapter", "Invalid position on delete: " + position);
            return;
        }
        ToDoModel item = mList.get(position);
        myDB.deleteTask(item.getId());

        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editTasks(int position){
        if (position < 0 || position >= mList.size()) {
            Log.e("ToDoAdapter", "Invalid position on edit: " + position);
            return;
        }
        ToDoModel item = mList.get(position);
        Bundle bundle =  new Bundle();
        bundle.putInt("Id" , item.getId());
        bundle.putString("Task" , item.getTask());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(),task.getTag());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
