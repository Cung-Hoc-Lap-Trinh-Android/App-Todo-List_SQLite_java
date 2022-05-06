package com.example.todolist.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.todolist.model.Task;
//3
@Database(entities = {Task.class}, version = 1, exportSchema = false)
public  abstract class TaskDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "Task.db";
    public abstract TaskDao taskDao();
    private static TaskDatabase mInstance;

    public static synchronized TaskDatabase getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = Room.databaseBuilder(mCtx, TaskDatabase.class, DATABASE_NAME) //biến môi trường- class RoomDatabase- Database Name
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }
}

