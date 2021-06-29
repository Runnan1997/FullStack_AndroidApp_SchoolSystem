package com.example.project_seg2105;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InstructorSet extends AppCompatActivity {
    CheckBox mon,tues,wed,thurs,fri,tenAM,onePM,fivePM,threePM,sevenPM,threeC,eightC,fifteenC;
    Button setCourseButton;
    TextView iname,cname,ccode;
    EditText course_des;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_course);

        setCourseButton = findViewById(R.id.setCourse);

        Bundle extras = getIntent().getExtras();
        String cName = getIntent().getStringExtra("coursename");
        String cCode = getIntent().getStringExtra("coursecode");
        String inName = getIntent().getStringExtra("insname");

        iname = (TextView)findViewById(R.id.in);
        cname = (TextView)findViewById(R.id.cn);
        ccode = (TextView)findViewById(R.id.cc);

        course_des = (EditText)findViewById(R.id.courseDes) ;

        iname.setText(inName);
        cname.setText(cName);
        ccode.setText(cCode);

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

        setCourseButton = findViewById(R.id.setCourse);

        setCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCourse();
            }
        });
        ref = FirebaseDatabase.getInstance().getReference("Course");
    }

    private void setCourse(){
        String insName = iname.getText().toString().trim();
        String coursename = cname.getText().toString().trim();
        String coursecode = ccode.getText().toString().trim();
        String courseDes = course_des.getText().toString().trim();
        String day = "";
        String time = "";
        String cap = "";

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

        if(tenAM.isChecked()){
            time = "10AM";
        }
        if(onePM.isChecked()){
            time = "1PM";
        }
        if(threePM.isChecked()){
            time = "3PM";
        }
        if(fivePM.isChecked()){
            time = "5PM";
        }
        if(sevenPM.isChecked()){
            time = "7PM";
        }

        if(threeC.isChecked()){
            cap = "30 people";
        }
        if(eightC.isChecked()){
            cap = "80 people";
        }
        if(fifteenC.isChecked()){
            cap = "150 people";
        }

        if(!TextUtils.isEmpty(coursecode)){
            String id = ref.push().getKey();
            final Course cour = new Course(id,coursename,coursecode,insName);
            String finalDay = day;
            String finalTime = time;
            String finalCap = cap;
            if(day != "" && time !="" && cap != "" && courseDes != "") {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Course dbcc = dataSnapshot.child(cour.getCourseCode()).getValue(Course.class);
                        ref.child(dbcc.getCourseCode()).removeValue();
                        Course cc = new Course(coursename, coursecode, insName, finalDay, finalTime, finalCap, courseDes);
                        ref.child(cc.getCourseCode()).setValue(cc);
                        Toast.makeText(InstructorSet.this, "The course is set now", Toast.LENGTH_SHORT).show();
                        mon.setChecked(false);
                        tues.setChecked(false);
                        wed.setChecked(false);
                        thurs.setChecked(false);
                        fri.setChecked(false);

                        tenAM.setChecked(false);
                        onePM.setChecked(false);
                        threePM.setChecked(false);
                        fivePM.setChecked(false);
                        sevenPM.setChecked(false);

                        threeC.setChecked(false);
                        eightC.setChecked(false);
                        fifteenC.setChecked(false);

                        course_des.setText("");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
            else {
                Toast.makeText(InstructorSet.this, "Please enter all the information", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this,"please enter all the information", Toast.LENGTH_LONG).show();
        }
    }
}
