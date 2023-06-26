package com.example.rebook.Activities;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;

import com.example.rebook.Models.User;
import com.example.rebook.R;

public class HomeAdmin extends Activity {

    private ImageView image;
    private Button logout,editProfile,addBooks,deleteBooks;
    private User user;
    private TextView greeting;
    private String password;
    private Bundle bundle;
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.home_admin);
        initViews();

        bundle= getIntent().getExtras();
        user = (User) getIntent().getSerializableExtra("user");
        password = bundle.getString("password");

        SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", user.getUser_email());
        editor.putString("password", password);
        editor.apply();

        ViewCompat.setBackgroundTintList(addBooks, null);
        ViewCompat.setBackgroundTintList(deleteBooks, null);
        ViewCompat.setBackgroundTintList(editProfile, null);
        ViewCompat.setBackgroundTintList(logout, null);

        if(user!=null){
            String text="Hi "+user.getUser_first_name();
            greeting.setText(text);
        }

        addBooks.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                displayAddBooks(user);
            }
        });

        deleteBooks.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                displayDeleteBooks(user);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                displayEditProfile(user);
            }
        });

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Logout();
            }
        });

    }


    private void initViews(){
        addBooks= findViewById(R.id.add_book);
        deleteBooks= findViewById(R.id.delete_book);
        editProfile= findViewById(R.id.edit_profile);
        logout=findViewById(R.id.logout);
        greeting= findViewById(R.id.greeting);

    }

    private void displayAddBooks(User user){
        Intent i = new Intent(HomeAdmin.this,AddBooksAdmin.class);
        i.putExtra("user",user);
        startActivity(i);
    }

    private void displayDeleteBooks(User user){
        Intent i = new Intent(HomeAdmin.this,RemoveBookAdmin.class);
        i.putExtra("user",user);
        startActivity(i);
    }

    private void displayEditProfile(User user){
        Intent i = new Intent(HomeAdmin.this,EditProfile.class);
        i.putExtra("user",user);
        startActivityForResult(i,3);
    }

    private void Logout(){
        SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("email");
        editor.remove("password");
        editor.apply();

        Intent i = new Intent(HomeAdmin.this, Login.class);
        Toast.makeText(HomeAdmin.this,"Logged out",Toast.LENGTH_SHORT).show();
        startActivity(i);
        finish();
    }

}