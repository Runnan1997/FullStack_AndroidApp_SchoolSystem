package com.example.project_seg2105;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import android.widget.CheckBox;


public class searchCourse extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference inscourse;
    EditText course_code;
    EditText course_name;
    EditText course_des;
    TextView iname;
    ListView course_list;
    Button assignCourseButton;
    Button unassignCourseButton;
    CheckBox mon,tues,wed,thurs,fri,tenAM,onePM,fivePM,threePM,sevenPM,threeC,eightC,fifteenC;

    DatabaseReference ref;
    List<Course> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_course);

        Bundle extras = getIntent().getExtras();

        mon = (CheckBox)findViewById(R.id.mon);
        tues = (CheckBox)findViewById(R.id.tues);
        wed = (CheckBox)findViewById(R.id.wed);
        thurs = (CheckBox)findViewById(R.id.thurs);
        fri = (CheckBox)findViewById(R.id.fri);

        tenAM = (CheckBox)findViewById(R.id.tenAM);
        onePM = (CheckBox)findViewById(R.id.onePM);
        threePM = (CheckBox)findViewById(R.id.threePM);
        fivePM = (CheckBox)findViewById(R.id.fivePM);
        sevenPM = (CheckBox)findViewById(R.id.sevenPM);

        threeC = (CheckBox)findViewById(R.id.smallCap);
        eightC = (CheckBox)findViewById(R.id.mediumCap);
        fifteenC = (CheckBox)findViewById(R.id.largeCap);

        course_code = (EditText)findViewById(R.id.courseCode) ;
        course_des = (EditText)findViewById(R.id.courseDes) ;
        course_name = (EditText)findViewById(R.id.courseName);
        assignCourseButton = findViewById(R.id.assignCourse);
        unassignCourseButton = findViewById(R.id.unassignCourse);

        assignCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignCourse();
            }
        });

        unassignCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unassignCourse();
            }
        });

        course_list = findViewById(R.id.courseList);

        list = new ArrayList<Course>();
        ref = FirebaseDatabase.getInstance().getReference("Course");

        database = FirebaseDatabase.getInstance();
        inscourse = database.getReference("InstructorCourse");

        final TextView courseC = (TextView)findViewById(R.id.coursecode);
        final TextView cooo = (TextView)findViewById(R.id.coursecode2);
        iname = (TextView)findViewById(R.id.i_Name);
        Intent intent = getIntent();
        String courseinfo = intent.getStringExtra(Instructor.EXTRA_TEXT);
        String username = intent.getStringExtra(Instructor.EXTRA_TEXT2);

        iname.setText(username);
        courseC.setText(courseinfo);
        TextView no = (TextView)findViewById(R.id.tvno);

        inscourse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Course ic = snapshot.child(courseinfo).getValue(Course.class);
                if(courseinfo.matches("[a-zA-Z]+")){
                    cooo.setText(ic.getCourseCode());
                }
                else{
                    cooo.setText(ic.getCourseName());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public static boolean matchformat(String course){
        return course.matches("[a-zA-Z]+");
    }

    private void assignCourse(){
        String coursename = course_name.getText().toString().trim();
        String coursecode = course_code.getText().toString().trim();
        String insName = iname.getText().toString().trim();

        if(!TextUtils.isEmpty(coursecode) && !TextUtils.isEmpty(coursename)){
            String id = ref.push().getKey();
            final Course cour = new Course(id, coursename,coursecode,insName);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(cour.getCourseCode()).exists()) {
                            Course dbcc = dataSnapshot.child(cour.getCourseCode()).getValue(Course.class);
                            if (dbcc.getiName() == null || dbcc.getiName().equals(insName)) {
                                Course cc = new Course(coursename, coursecode, insName);
                                ref.child(cc.getCourseCode()).setValue(cc);
                                Intent passname = new Intent(getApplicationContext(),InstructorSet.class);
                                passname.putExtra("coursename",coursename);
                                passname.putExtra("coursecode", coursecode);
                                passname.putExtra("insname", insName);
                                startActivity(passname);
                                Toast.makeText(searchCourse.this, "The course is assigned to you", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(searchCourse.this, "The course is already assigned.", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(searchCourse.this, "The course does not exist.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                course_name.setText("");
                course_code.setText("");
            }
            else {
                Toast.makeText(searchCourse.this, "Please enter all the information", Toast.LENGTH_SHORT).show();
            }
    }

    private void unassignCourse() {
        String coursename = course_name.getText().toString().trim();
        String coursecode = course_code.getText().toString().trim();
        String insName = iname.getText().toString().trim();

        Intent intent = getIntent();
        String username = intent.getStringExtra(Instructor.EXTRA_TEXT2);

        if (!TextUtils.isEmpty(coursecode)) {
            String id = ref.push().getKey();
            final Course cour = new Course(id, coursename, coursecode, insName);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(cour.getCourseCode()).exists()) {
                        Course dbcc = dataSnapshot.child(cour.getCourseCode()).getValue(Course.class);
                        if(dbcc.getiName() == null){
                            Toast.makeText(searchCourse.this, "The course is not your course", Toast.LENGTH_SHORT).show();
                        }
                        else if(dbcc.getiName().equals(username)){
                            ref.child(dbcc.getCourseCode()).removeValue();
                            Course cc = new Course(coursecode, coursename);
                            ref.child(cc.getCourseCode()).setValue(cc);
                            Toast.makeText(searchCourse.this, "The course is unassigned now", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(searchCourse.this, "The course is not your course", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(searchCourse.this, "The course does not exist, please add it.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            course_name.setText("");
            course_code.setText("");
        }
        else{
            Toast.makeText(searchCourse.this, "please enter all the information.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot d : snapshot.getChildren()){
                    Course co = d.getValue(Course.class);
                    list.add(co);
                }
                CourseList courseAdapter = new CourseList(searchCourse.this, list);
                course_list.setAdapter(courseAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
