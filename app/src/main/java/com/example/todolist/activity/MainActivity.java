package com.example.todolist.activity;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.todolist.R;
import com.example.todolist.adapter.TaskAdapter;
import com.example.todolist.bottomSheetFragment.CreateTaskBottomSheetFragment;

import com.example.todolist.bottomSheetFragment.ShowCalendarViewBottomSheet;
import com.example.todolist.database.TaskDatabase;
import com.example.todolist.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements CreateTaskBottomSheetFragment.setRefreshListener {

//    public static final String CHANNEL_ID ="channel1";

    @BindView(R.id.taskRecycler)
    RecyclerView taskRecycler;

    @BindView(R.id.btnAddTask)
    FloatingActionButton addTask;

    TaskAdapter taskAdapter;
    List<Task> tasks = new ArrayList<>();

    @BindView(R.id.noDataImage)
    ImageView noDataImage;


    @Override                                               //nạp chồng
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);          //gọi đến giao diện liên kết - chèn giao diện vào Activity
        ButterKnife.bind(this);
        setUpAdapter();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);


        Glide.with(getApplicationContext()).load(R.drawable.new_todo).into(noDataImage);

        addTask.setOnClickListener(view -> {
            CreateTaskBottomSheetFragment createTaskBottomSheetFragment = new CreateTaskBottomSheetFragment();
            createTaskBottomSheetFragment.setTaskId(0, false, this, MainActivity.this);
            createTaskBottomSheetFragment.show(getSupportFragmentManager(), createTaskBottomSheetFragment.getTag());
        });

        getSavedTasks();


    }


    public void setUpAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        //Hiển thị danh sách
        taskAdapter = new TaskAdapter(this, tasks, this);
        taskRecycler.setLayoutManager(linearLayoutManager);
        taskRecycler.setAdapter(taskAdapter);
    }

    private void getSavedTasks() {

        class GetSavedTasks extends AsyncTask<Void, Void, List<Task>> {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                tasks = TaskDatabase
                        .getInstance(getApplicationContext())
                        .taskDao()
                        .getAllTasksList();
                return tasks;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                noDataImage.setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
                setUpAdapter();
            }
        }

        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }


    @Override
    public void refresh() {
        getSavedTasks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));

        SearchView.SearchAutoComplete theTextArea = (SearchView.SearchAutoComplete)searchView.findViewById(R.id.search_src_text);
        theTextArea.setTextColor(getResources().getColor(R.color.colorAccent));//or any color that you want
        theTextArea.setHintTextColor(getResources().getColor(R.color.colorAccent));
        //change icon color
        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        searchIcon.setColorFilter(getResources().getColor(R.color.colorAccent));
        ImageView searchIconClose = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchIconClose.setColorFilter(getResources().getColor(R.color.colorAccent));

        //Event
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //Clear text when click to Clear button on Search View
        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            EditText ed = (EditText) searchView.findViewById(R.id.search_src_text);
            //Clear Text
            ed.setText("");
            //Clear Query
            searchView.setQuery("", false);
            //Collapse the action view
            searchView.onActionViewCollapsed();
            //Collapse the search widget
            menuItem.collapseActionView();
            //Restore result to original
            setUpAdapter();
//            menuItemCal.setEnabled(true);

        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_calendar) {
            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
            ShowCalendarViewBottomSheet showCalendarViewBottomSheet = new ShowCalendarViewBottomSheet();
            showCalendarViewBottomSheet.show(getSupportFragmentManager(), showCalendarViewBottomSheet.getTag());

        }
        return super.onOptionsItemSelected(item);
    }

    private void startSearch(String query) {
        List<Task> searchList = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.getTaskTitle().toLowerCase().contains(query)) {
                searchList.add(task);

            }
            taskAdapter = new TaskAdapter(this, searchList, this);
            taskRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            taskRecycler.setAdapter(taskAdapter);
        }

    }
}
