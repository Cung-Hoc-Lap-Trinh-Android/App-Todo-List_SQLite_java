package com.example.todolist.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//1
@Entity
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)                  //trường TaskId là khóa chính và tự động tăng
    int taskId;
    @ColumnInfo(name = "taskTitle")                     //khai báo tên cột
    String taskTitle;
    @ColumnInfo(name = "date")
    String date;
    @ColumnInfo(name = "taskDescription")
    String taskDescrption;
    @ColumnInfo(name = "isComplete")
    int isComplete;
    @ColumnInfo(name = "lastAlarm")
    String lastAlarm;


    public Task() {}                                            //constructor

    public int getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }

    public String getLastAlarm() {
        return lastAlarm;
    }

    public void setLastAlarm(String lastAlarm) {
        this.lastAlarm = lastAlarm;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTaskDescrption() {
        return taskDescrption;
    }

    public void setTaskDescrption(String taskDescrption) {
        this.taskDescrption = taskDescrption;
    }
}
