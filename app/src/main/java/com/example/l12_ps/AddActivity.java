package com.example.l12_ps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    EditText etName, etDescription, etTime;
    Button btnAdd, btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etTime = findViewById(R.id.etTime);
        btnAdd = findViewById(R.id.btnAddTask);
        btnCancel = findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etDescription.getText().toString().equals("") && !etName.getText().toString().equals("")) {
                    String nameInput = etName.getText().toString();
                    String descriptionInput = etDescription.getText().toString();
                    int remind = Integer.parseInt(etTime.getText().toString());

                    try {
                        DBHelper dbh = new DBHelper(AddActivity.this);

                        Task task = new Task(nameInput, descriptionInput);
                        dbh.insertTask(task);
                        dbh.close();

                        int requestCode = 123;

                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.SECOND, remind);

                        Intent intent = new Intent(AddActivity.this, NotificationReceiver.class);
                        intent.putExtra("name", nameInput);
                        intent.putExtra("description", descriptionInput);
                        intent.putExtra("id", task.getId());
                        System.out.println("Test12" + nameInput + descriptionInput + task.getId());
                        Log.d("Test12", nameInput + descriptionInput + task.getId());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddActivity.this, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                        AlarmManager am = (AlarmManager) getSystemService(AddActivity.ALARM_SERVICE);
                        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

                        finish();
                        return;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
//                Intent intentBack = new Intent(AddActivity.this, MainActivity.class);
//                startActivity(intentBack);
                Toast.makeText(AddActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentBack = new Intent(AddActivity.this, MainActivity.class);
//                startActivity(intentBack);
                finish();
            }
        });
    }
}