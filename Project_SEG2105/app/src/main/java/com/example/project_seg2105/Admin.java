package com.example.project_seg2105;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin extends AppCompatActivity {
    EditText course_name;
    EditText course_code;
    EditText user_id;
    Button createCourseButton;
    Button updateCourseButton;
    Button deleteCourseButton;
    Button deleteUserButton;
    ListView course_list;

    //Firebase
    DatabaseReference ref;
    List<Course> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin);

        course_name = findViewById(R.id.courseName);
        course_code = findViewById(R.id.courseCode);
        user_id = findViewById(R.id.userID);
        createCourseButton = findViewById(R.id.createCourseButton);
        updateCourseButton = findViewById(R.id.updateCourseButton);
        deleteCourseButton = findViewById(R.id.deleteCourseButton);
        deleteUserButton = findViewById(R.id.deleteUserButton);
        course_list = findViewById(R.id.courseList);

        list = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("Course");

        createCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourse();
            }
        });

        updateCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCourse();
            }
        });

        deleteCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCourse();
            }
        });

        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser(user_id.getText().toString().trim());
            }
        });
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
                CourseList courseAdapter = new CourseList(Admin.this, list);
                course_list.setAdapter(courseAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void addCourse(){
        String coursename = course_name.getText().toString().trim();
        String coursecode = course_code.getText().toString().trim();

        if(!TextUtils.isEmpty(coursename)){
            String id = ref.push().getKey();
            final Course cour = new Course(id, coursecode, coursename);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(cour.getCourseCode()).exists()){
                        Toast.makeText(Admin.this, "The course already exsist", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Course cc = new Course(coursecode, coursename);
                        ref.child(cc.getCourseCode()).setValue(cc);
                        Toast.makeText(Admin.this, "Course added", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
                   

        course_name.setText("");
            course_code.setText("");

            //Toast.makeText(this,"Course added", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"please enter course name and code", Toast.LENGTH_LONG).show();
        }
    }

    private void updateCourse(){
        String coursename = course_name.getText().toString().trim();
        String coursecode = course_code.getText().toString().trim();

        if(!TextUtils.isEmpty(coursename)){
            String id = ref.push().getKey();
            final Course cour = new Course(id, coursecode, coursename);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(cour.getCourseCode()).exists()){
                        Course cc = new Course(coursecode, coursename);
                        ref.child(cc.getCourseCode()).setValue(cc);
                        Toast.makeText(Admin.this, "The course is updated now", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Admin.this, "The course does not exist, please add it.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            course_name.setText("");
            course_code.setText("");

            //Toast.makeText(this,"Course added", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"please enter course name and code", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteUser(String userID){
        if(!TextUtils.isEmpty(userID)){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            DatabaseReference myRef = ref.child("Users").child(userID);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()){
                        Toast.makeText(Admin.this,"User does not exist", Toast.LENGTH_LONG).show();
                        System.out.println(userID);
                    }
                    else{
                        dataSnapshot.getRef().removeValue();
                        Toast.makeText(Admin.this,"User is deleted now", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            user_id.setText("");
        }
        else{
            Toast.makeText(this,"please enter a user name", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteCourse(){
        String coursename = course_name.getText().toString().trim();
        String coursecode = course_code.getText().toString().trim();

        if(!TextUtils.isEmpty(coursename)){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            DatabaseReference myRef = ref.child("Course").child(coursecode);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()){
                        Toast.makeText(Admin.this,"The course does not exist", Toast.LENGTH_LONG).show();
                    }
                    else{
                        dataSnapshot.getRef().removeValue();
                        Toast.makeText(Admin.this,"Course is deleted now", Toast.LENGTH_LONG).show();
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
            Toast.makeText(this,"please enter course name and code", Toast.LENGTH_LONG).show();
        }
    }
}