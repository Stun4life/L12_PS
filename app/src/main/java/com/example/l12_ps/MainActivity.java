package com.example.l12_ps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView tasksListView;
    private ArrayAdapter<Task> arrayAdapter;
    private ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListComps();
        loadTasks();
    }
    private void initViews() {
        Button addTaskBtn = findViewById(R.id.btnAddTask);
        addTaskBtn.setOnClickListener(this);
    }

    private void initListComps() {
        tasksListView = findViewById(R.id.listViewTasks);
        tasks = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, tasks);
        tasksListView.setAdapter(arrayAdapter);
    }

    private void loadTasks() {
        DBHelper db = new DBHelper(this);
        tasks.addAll(db.getAllTasks());
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intentreceived = getIntent();
        String name = intentreceived.getExtras().getString("name");
        String description = intentreceived.getExtras().getString("description");
        int id = intentreceived.getExtras().getInt("id");

        DBHelper dbh = new DBHelper(MainActivity.this);
        dbh.deleteTask(id);
        dbh.close();
    }

    @Override
    public void onClick(View v) {
        // TODO: go to add activity
        Intent intentAdd = new Intent(MainActivity.this, AddActivity.class);
        startActivity(intentAdd);
    }
}