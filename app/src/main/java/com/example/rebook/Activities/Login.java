package com.example.rebook.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;


import com.example.rebook.AsyncTasks.GetUsersAPI;
import com.example.rebook.Models.User;
import com.example.rebook.R;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;




public class Login extends Activity {
    private EditText email, password;
    private TextView error;
    private Button login,back;
    private GetUsersAPI u;
    private boolean found = false;
    private Bundle bundle;
    ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        bundle = getIntent().getExtras();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        error = findViewById(R.id.error);
        login = findViewById(R.id.login);
        back = findViewById(R.id.back_btn);

        ViewCompat.setBackgroundTintList(login, null);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u = new GetUsersAPI(Login.this);

                try {
                    users = u.execute().get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                String emailInput = email.getText().toString().trim();
                String passInput = password.getText().toString().trim();


                    found = false;
                    for (User u : users) {
                        String hashedPassword = u.getUser_password();

                        if (u.getUser_email().equals(emailInput) && BCrypt.checkpw(passInput, hashedPassword)) {
                            if (error.getVisibility() == View.VISIBLE) {
                                error.setVisibility(View.GONE);
                            }
                            found = true;
                            displayHome(u);
                            break;
                        }
                    }

                    if (!found) {
                        error.setVisibility(View.VISIBLE);
                        error.setText("Wrong credentials");
                    }
                }

        });


        SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        String emailPref = preferences.getString("email", null);
        String passwordPref = preferences.getString("password", null);

        if (emailPref != null && passwordPref != null) {
            email.setText(emailPref);
            password.setText(passwordPref);
            login.performClick();
        }


        TextView createAccountLink = findViewById((R.id.register));
        TextView facebookSignInLink = findViewById(R.id.facebookSignInLink);
        TextView googleSignInLink = findViewById(R.id.googleSignInLink);


        createAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Register activity
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });



        facebookSignInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              Toast.makeText(Login.this,"Cannot perform action: feature not added",Toast.LENGTH_SHORT).show();
            }
        });

        googleSignInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this,"Cannot perform action: feature not added",Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void displayHome(User u) {

        if(u.getRole_id()==1){
          Intent i  = new Intent(Login.this, Home.class);
            i.putExtra("user", u);
            i.putExtra("password", password.getText().toString().trim());
            startActivity(i);
        }
       else if (u.getRole_id()==2){
           Intent i = new Intent(Login.this,HomeAdmin.class);
            i.putExtra("user", u);
            i.putExtra("password", password.getText().toString().trim());
            startActivity(i);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        String emailPref = preferences.getString("email", null);
        String passwordPref = preferences.getString("password", null);

        if (emailPref == null && passwordPref == null) {
            email.setText("");
            password.setText("");
            error.setVisibility(View.GONE);
        }

    }
}

