package com.example.rebook.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rebook.R;

public class Welcome extends Activity {
    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);

        SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        String emailPref = preferences.getString("email", null);
        String passwordPref = preferences.getString("password", null);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });
        if (emailPref != null && passwordPref != null) {
            loginButton.performClick();
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle register button click
                openRegisterActivity();
            }
        });
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}

