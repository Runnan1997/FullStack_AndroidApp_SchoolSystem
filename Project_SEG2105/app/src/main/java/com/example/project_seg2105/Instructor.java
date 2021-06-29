package com.example.project_seg2105;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Instructor extends AppCompatActivity {
    TextView hoName;
    Button searchspN;
    Button searchspC;
    EditText search_courseC;
    EditText search_courseN;
    FirebaseDatabase database;
    DatabaseReference inscourse;
    public static final String EXTRA_TEXT = "homeownernameinHomeOwner";
    public static final String EXTRA_TEXT2 = "instructorName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);

        Bundle extras = getIntent().getExtras();

        hoName = (TextView)findViewById(R.id.tvhoname);
        search_courseC = (EditText)findViewById(R.id.searchcourseC);
        search_courseN = (EditText)findViewById(R.id.searchcourseN);
        Intent intent = getIntent();
        String honame = intent.getStringExtra(Home.EXTRA_TEXT1);
        hoName.setText(honame);

        database = FirebaseDatabase.getInstance();
        inscourse = database.getReference("InstructorCourse");

        searchspN = (Button)findViewById(R.id.btnserachspN);
        searchspC = (Button)findViewById(R.id.btnserachspC);

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
                                Intent intent = new Intent(getApplicationContext(), searchCourse.class);
                                intent.putExtra(EXTRA_TEXT, courseN);
                                intent.putExtra(EXTRA_TEXT2, username);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(Instructor.this, "This course does not exist", Toast.LENGTH_SHORT).show();
                        }
                        search_courseN.setText("");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            else{
                Toast.makeText(Instructor.this, "Please enter a valid course name", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(Instructor.this, "Please enter all the information", Toast.LENGTH_SHORT).show();
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
                                Intent intent = new Intent(getApplicationContext(), searchCourse.class);
                                intent.putExtra(EXTRA_TEXT, courseC);
                                intent.putExtra(EXTRA_TEXT2, username);
                                startActivity(intent);

                            }
                        } else {
                            Toast.makeText(Instructor.this, "This course does not exist", Toast.LENGTH_SHORT).show();
                        }
                        search_courseC.setText("");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            else{
                Toast.makeText(Instructor.this, "Please enter a valid course code", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(Instructor.this, "Please enter all the information", Toast.LENGTH_SHORT).show();
        }
    }
}
