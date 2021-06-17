package com.example.project_seg2105;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText CreateAccount;
    private Button Administrator;
    private Button Instructor;
    private Button Student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreateAccount = (EditText) findViewById(R.id.createaccount);
        Administrator = (Button) findViewById(R.id.administrator);
        Instructor = (Button) findViewById(R.id.homeOwner);
        Student = (Button) findViewById(R.id.serviceProvider);

        Administrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent passdata = new Intent(getApplicationContext(),SignUp.class);
                passdata.putExtra("roletype", "Administrator");
                startActivity(passdata);
            }

        });


        Instructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent passdata = new Intent(getApplicationContext(),SignUp.class);
                passdata.putExtra("roletype", "Instructor");
                startActivity(passdata);
            }
        });

        Student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent passdata = new Intent(getApplicationContext(),SignUp.class);
                passdata.putExtra("roletype", "Student");
                startActivity(passdata);
            }
        });
    }



}