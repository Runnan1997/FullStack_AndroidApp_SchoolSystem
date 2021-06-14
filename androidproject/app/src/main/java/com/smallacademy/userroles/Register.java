package com.smallacademy.userroles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText fullName, username, password;
    Button registerBtn, goToLogin;
    CheckBox IsInstructor, IsStudent;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullName = findViewById(R.id.registerName);
        username = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        registerBtn = findViewById(R.id.registerBtn);
        goToLogin = findViewById(R.id.gotoLogin);
        IsInstructor = findViewById(R.id.isTeacher);
        IsStudent = findViewById(R.id.isStudent);
        //Make it so only one checkbox can be chosen
        //If student box is chosen, teacher will not be checked
        //If Instructor box is chosen, the student box will not be checked
        IsStudent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    IsInstructor.setChecked(false);
                }
            }
        });
        IsInstructor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    IsStudent.setChecked(false);
                }
            }
        });
        registerBtn.setOnClickListener(v -> {
            checkField(fullName);
            checkField(username);
            checkField(password);
            //checkbox validation. If no boxes are checked, it would return true
            if(!(IsInstructor.isChecked()||IsStudent.isChecked())){
                Toast.makeText(Register.this, "Select The Account Type Please", Toast.LENGTH_SHORT).show();
                //return to the same activity
                return;
            }
            //we are good to go and the user has filled out all information
            if (valid) {
                //start the user registration process to firebase
                fAuth.createUserWithEmailAndPassword(username.getText().toString()+"@whateverdomain.com", password.getText().toString()).addOnSuccessListener(authResult -> {
                    FirebaseUser user = fAuth.getCurrentUser();
                    Toast.makeText(Register.this, "Account Created", Toast.LENGTH_SHORT).show();
                    DocumentReference df = fStore.collection("Users").document(user.getUid());
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("FullName", fullName.getText().toString());
                    userInfo.put("Username", username.getText().toString());
                    //specify if the user is an admin
                    if(IsInstructor.isChecked()){
                        userInfo.put("isInstructor","1");
                    }
                    if(IsStudent.isChecked()){
                        userInfo.put("isStudent","1");
                    }
                    df.set(userInfo);
                    //sends the user to their respective account
                    if (IsInstructor.isChecked()){
                        startActivity(new Intent(getApplicationContext(), Admin.class));
                        finish();
                    }
                    if(IsStudent.isChecked()){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(e -> Toast.makeText(Register.this, "Failed to create account, Please try again", Toast.LENGTH_SHORT).show());
            }

        });
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity((new Intent(getApplicationContext(),Login.class)));
            }
        });
    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }
}