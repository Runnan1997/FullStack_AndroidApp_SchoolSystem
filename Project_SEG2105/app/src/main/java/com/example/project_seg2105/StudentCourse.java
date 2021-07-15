package com.example.project_seg2105;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StudentCourse extends AppCompatActivity {
    EditText course_code;
    EditText course_name;
    TextView iname;
    TextView iname2;
    ListView course_list;
    Button enrollCourseButton;
    Button unenrollCourseButton;

    FirebaseDatabase database;
    DatabaseReference inscourse;

    DatabaseReference ref;
    List<Course> list;

    public static final String EXTRA_TEXT = "homeownernameinHomeOwner";
    public static final String EXTRA_TEXT2 = "instructorName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course);

        Bundle extras = getIntent().getExtras();

        course_code = (EditText)findViewById(R.id.courseCode) ;
        course_name = (EditText)findViewById(R.id.courseName);
        enrollCourseButton = findViewById(R.id.enroll);
        unenrollCourseButton = findViewById(R.id.unenroll);

        iname = (TextView)findViewById(R.id.i_Name);
        iname2 = (TextView)findViewById(R.id.i_Name2);

        course_list = findViewById(R.id.courseList);

        list = new ArrayList<Course>();
        ref = FirebaseDatabase.getInstance().getReference("Course");

        Intent intent = getIntent();
        String courseinfo = intent.getStringExtra(Student.EXTRA_TEXT);
        System.out.println(courseinfo);
        String username = intent.getStringExtra(Student.EXTRA_TEXT2);

        enrollCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enrollCourse();
            }
        });

        unenrollCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unenrollCourse();
            }
        });

        database = FirebaseDatabase.getInstance();
        inscourse = database.getReference("InstructorCourse");
        final TextView courseC = (TextView)findViewById(R.id.coursecode);
        final TextView cooo = (TextView)findViewById(R.id.coursecode2);

        System.out.println("username in sc " +username);

        inscourse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(courseinfo != null && courseinfo.matches("[a-zA-Z]+")){
                    Course ic = snapshot.child(courseinfo).getValue(Course.class);
                    cooo.setText(ic.getCourseCode());
                    courseC.setText(ic.getCourseName());
                }
                else if(courseinfo.contains("monday") ||courseinfo.contains("tuesday")||courseinfo.contains("wednesday")||courseinfo.contains("thursday")||courseinfo.contains("friday")){
                    iname.setText(courseinfo);
                    iname2.setText(username);
                }
                else{
                    Course ic = snapshot.child(courseinfo).getValue(Course.class);
                    cooo.setText(ic.getCourseName());
                    courseC.setText(ic.getCourseCode());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void enrollCourse(){
        String coursename = course_name.getText().toString().trim();
        String coursecode = course_code.getText().toString().trim();
        String sName = iname2.getText().toString().trim();

        if(!TextUtils.isEmpty(coursecode) && !TextUtils.isEmpty(coursename)){
            String id = ref.push().getKey();
            final CourseForStudent cour = new CourseForStudent(id,coursecode,sName);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(cour.getCourseCode()).exists()) {
                        Course dbcc = dataSnapshot.child(cour.getCourseCode()).getValue(Course.class);
                        String iName = dbcc.getiName();
                        String itime = dbcc.getTime();
                        String iday = dbcc.getDay();
                        String icap = dbcc.getCapacity();
                        String ides = dbcc.getCourseDes();
                        if (dbcc.getsName() == null || dbcc.getsName().equals(sName)) {
                            System.out.println("TEST SNAME " + dbcc.getsName());
                            List<String> ds = collectDays((Map<String,Object>) dataSnapshot.getValue());
                            System.out.println(ds);
                            boolean timeconflict = findDuplicates(ds);
                            System.out.println(timeconflict);
                            if(!timeconflict) {
                                CourseForStudent cc = new CourseForStudent(coursename, coursecode, sName, iName, iday, itime, icap, ides);
                                ref.child(cc.getCourseCode()).setValue(cc);
                                Intent passname = new Intent(getApplicationContext(), StudentCourseList.class);
                                passname.putExtra(EXTRA_TEXT, coursename);
                                passname.putExtra(EXTRA_TEXT2, sName);
                                startActivity(passname);
                                Toast.makeText(StudentCourse.this, "The course is assigned to you", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(StudentCourse.this, "The course has time conflict with other courses.", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            Toast.makeText(StudentCourse.this, "The course is already assigned.", Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        Toast.makeText(StudentCourse.this, "The course does not exist.", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(StudentCourse.this, "Please enter all the information", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList collectDays(Map<String,Object> c) {

        ArrayList<String> days = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : c.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            String info = (String) singleUser.get("day") + " " + (String) singleUser.get("time");
            days.add(info);
        }
        return days;
    }

    public static boolean findDuplicates(List<String> listContainingDuplicates) {

        final Set<String> setToReturn = new HashSet<String>();
        final Set<String> set1 = new HashSet<String>();

        for (String yourInt : listContainingDuplicates) {
            if (!set1.add(yourInt)) {
                setToReturn.add(yourInt);
            }
        }
        setToReturn.remove("null null");
        System.out.println("DUPLICATE " + setToReturn);
        if(setToReturn.isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }

    public void unenrollCourse(){
        String coursename = course_name.getText().toString().trim();
        String coursecode = course_code.getText().toString().trim();
        String sName = iname2.getText().toString().trim();

        Intent intent = getIntent();
        String username = intent.getStringExtra(Student.EXTRA_TEXT2);

        if (!TextUtils.isEmpty(coursecode)) {
            String id = ref.push().getKey();
            final CourseForStudent cour = new CourseForStudent(id, coursecode, sName);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(cour.getCourseCode()).exists()) {
                        CourseForStudent dbcc = dataSnapshot.child(cour.getCourseCode()).getValue(CourseForStudent.class);
                        if(dbcc.getsName() == null){
                            Toast.makeText(StudentCourse.this, "The course is not your course", Toast.LENGTH_SHORT).show();
                        }
                        else if(dbcc.getsName().equals(username)){
                            String iName = dbcc.getiName();
                            String itime = dbcc.getTime();
                            String iday = dbcc.getDay();
                            String icap = dbcc.getCapacity();
                            String ides = dbcc.getCourseDes();
                            ref.child(dbcc.getCourseCode()).removeValue();
                            Course cc = new Course(coursename, coursecode, iName, iday, itime, icap, ides);
                            ref.child(cc.getCourseCode()).setValue(cc);
                            Toast.makeText(StudentCourse.this, "The course is unassigned now", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(StudentCourse.this, "The course is not your course", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(StudentCourse.this, "The course does not exist, please add it.", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(StudentCourse.this, "please enter all the information.", Toast.LENGTH_SHORT).show();
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
                CourseList courseAdapter = new CourseList(StudentCourse.this, list);
                course_list.setAdapter(courseAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}