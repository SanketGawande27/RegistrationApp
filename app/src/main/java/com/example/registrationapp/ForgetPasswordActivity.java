package com.example.registrationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText emailID;
    private Button forgetBtn;

    String forgetEmailid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        emailID=(EditText) findViewById(R.id.txtForgetUsername);
        forgetBtn= (Button) findViewById(R.id.btn_forgetPassword);

        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetEmailid = emailID.getText().toString();
                if(forgetEmailid.isEmpty()){
                    Toast.makeText(ForgetPasswordActivity.this, "Please enter registered email", Toast.LENGTH_SHORT).show();
                }else {
                    forgetPassword();
                }

            }
        });



    }

    private void forgetPassword() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(forgetEmailid).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ForgetPasswordActivity.this, "Please check your email to reset password", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgetPasswordActivity.this,Login.class));
                        finish();
                    }else {
                        Toast.makeText(ForgetPasswordActivity.this, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }
}