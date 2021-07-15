package com.example.project_seg2105;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class Student extends AppCompatActivity {
    TextView hoName;
    Button searchspN;
    Button searchspC;
    Button searchday;
    EditText search_courseC;
    EditText search_courseN;
    FirebaseDatabase database;
    DatabaseReference inscourse;
    DatabaseReference course;
    public static final String EXTRA_TEXT = "homeownernameinHomeOwner";
    public static final String EXTRA_TEXT2 = "instructorName";
    CheckBox mon,tues,wed,thurs,fri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Bundle extras = getIntent().getExtras();

        hoName = (TextView)findViewById(R.id.tvhoname);
        search_courseC = (EditText)findViewById(R.id.searchcourseC);
        search_courseN = (EditText)findViewById(R.id.searchcourseN);
        Intent intent = getIntent();
        final String username = getIntent().getStringExtra("username");
        hoName.setText(username);

        database = FirebaseDatabase.getInstance();
        inscourse = database.getReference("InstructorCourse");
        course = database.getReference().child("Course");

        searchspN = (Button)findViewById(R.id.btnserachspN);
        searchspC = (Button)findViewById(R.id.btnserachspC);
        searchday = (Button)findViewById(R.id.seachbyday);

        mon = (CheckBox)findViewById(R.id.mon);
        tues = (CheckBox)findViewById(R.id.tues);
        wed = (CheckBox)findViewById(R.id.wed);
        thurs = (CheckBox)findViewById(R.id.thurs);
        fri = (CheckBox)findViewById(R.id.fri);

        searchspN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCourseN(search_courseN.getText().toString().trim());
            }
        });

        searchspC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCourseC(search_courseC.getText().toString().trim());
            }
        });

        searchday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCourseD();
            }
        });

    }

    public void searchCourseD(){
        String day = "";

        if(mon.isChecked()){
            day = "monday";
        }
        if(tues.isChecked()){
            day = "tuesday";
        }
        if(wed.isChecked()){
            day = "wednesday";
        }
        if(thurs.isChecked()){
            day = "thursday";
        }
        if(fri.isChecked()){
            day = "friday";
        }
        if(day != ""){
            String finalDay = day;
            course.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Get map of users in datasnapshot
                            ArrayList<String> ds = collectDays((Map<String,Object>) dataSnapshot.getValue());
                            ArrayList<String> info = new ArrayList<>();
                            for(int i = 0; i <ds.toArray().length; i++){
                                if(ds.get(i).contains(finalDay)){
                                    info.add(ds.get(i));
                                }
                                else{
                                    Toast.makeText(Student.this, "No courses available on this day", Toast.LENGTH_SHORT).show();
                                }
                            }
                            String x = "";
                            for(int j = 0; j < info.size(); j++){
                                x = x + " " + info.get(j);
                            }
                            System.out.println("ALL ABOUT WEEKDAYS " + x);
                            Intent intent = new Intent(getApplicationContext(), StudentCourse.class);
                            intent.putExtra(EXTRA_TEXT, x);
                            intent.putExtra(EXTRA_TEXT2, hoName.getText().toString());
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                        }
                    });
        }
        else{
            Toast.makeText(Student.this, "Please select a day", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList collectDays(Map<String,Object> c) {

        ArrayList<String> days = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : c.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            String cinfo = (String) singleUser.get("day") + " " + (String) singleUser.get("courseCode");
            days.add(cinfo);
        }

        return days;
    }

    public void searchCourseN(String courseN){
        if (!TextUtils.isEmpty(courseN)) {
            if(courseN.matches("^[a-zA-Z]*$")) {
                inscourse.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Course ic = dataSnapshot.child(courseN).getValue(Course.class);
                        if (dataSnapshot.child(courseN).exists()) {
                            if (ic.getCourseName().equals(courseN)) {
                                final String username = getIntent().getStringExtra("username");
                                Intent intent = new Intent(getApplicationContext(), StudentCourse.class);
                                intent.putExtra(EXTRA_TEXT, courseN);
                                intent.putExtra(EXTRA_TEXT2, username);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(Student.this, "This course does not exist", Toast.LENGTH_SHORT).show();
                        }
                        search_courseN.setText("");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            else{
                Toast.makeText(Student.this, "Please enter a valid course name", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(Student.this, "Please enter all the information", Toast.LENGTH_SHORT).show();
        }
    }

    public void searchCourseC(String courseC){
        if (!TextUtils.isEmpty(courseC)) {
            if(courseC.matches("^[a-zA-Z0-9]+$")) {
                inscourse.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Course ic = dataSnapshot.child(courseC).getValue(Course.class);
                        if (dataSnapshot.child(courseC).exists()) {
                            if (ic.getCourseCode().equals(courseC)) {
                                final String username = getIntent().getStringExtra("username");
                                Intent intent = new Intent(getApplicationContext(), StudentCourse.class);
                                intent.putExtra(EXTRA_TEXT, courseC);
                                intent.putExtra(EXTRA_TEXT2, username);
                                startActivity(intent);

                            }
                        } else {
                            Toast.makeText(Student.this, "This course does not exist", Toast.LENGTH_SHORT).show();
                        }
                        search_courseC.setText("");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            else{
                Toast.makeText(Student.this, "Please enter a valid course code", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(Student.this, "Please enter all the information", Toast.LENGTH_SHORT).show();
        }
    }
}
