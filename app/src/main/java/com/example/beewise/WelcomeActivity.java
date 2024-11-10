package com.example.beewise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button loginButton;
    private CheckBox showpassword;

    @Override
    @SuppressLint("MissingInflatedId")

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        showpassword = findViewById(R.id.showpw);

        showpassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show Password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                // Hide Password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getPassword = password.getText().toString();
                if (getPassword.equals("admin")) {
                    Intent intent = new Intent(WelcomeActivity.this, DashboardActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}