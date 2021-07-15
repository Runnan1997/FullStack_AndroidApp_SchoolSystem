package com.example.project_seg2105;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class StudentCourseList extends AppCompatActivity {
    TextView iname;
    TextView iname2;

    FirebaseDatabase database;
    DatabaseReference inscourse;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course_list);

        iname = (TextView)findViewById(R.id.i_Name);
        iname2 = (TextView)findViewById(R.id.i_Name2);

        ref = FirebaseDatabase.getInstance().getReference("Course");

        Intent intent = getIntent();
        String courseinfo = intent.getStringExtra(Student.EXTRA_TEXT);
        System.out.println(courseinfo);
        String username = intent.getStringExtra(Student.EXTRA_TEXT2);
        System.out.println("username in scl "+username);

        database = FirebaseDatabase.getInstance();
        inscourse = database.getReference("Course");

        final TextView courseC = (TextView)findViewById(R.id.coursecode);
        final TextView cooo = (TextView)findViewById(R.id.coursecode2);

        inscourse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Course ic = snapshot.child(courseinfo).getValue(Course.class);
//                    cooo.setText(ic.getCourseCode());
//                    courseC.setText(ic.getCourseName());

                ArrayList<String> ds = collectDays((Map<String,Object>) snapshot.getValue());
                ArrayList<String> info = new ArrayList<>();
                for(int i = 0; i < ds.size(); i++){
                    if(ds.get(i).contains(username)){
                        info.add(ds.get(i));
                    }
                }
                String x = "";
                for(int j = 0; j < info.size(); j++){
                    x = x + " " + info.get(j);
                }
                String y = x.replace(username,"");
                courseC.setText(y);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private ArrayList collectDays(Map<String,Object> c) {

        ArrayList<String> days = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : c.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            String cinfo = (String) singleUser.get("sName") + " " + (String) singleUser.get("courseName");
            days.add(cinfo);
        }

        return days;
    }
}