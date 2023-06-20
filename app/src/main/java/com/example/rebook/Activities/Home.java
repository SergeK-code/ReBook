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

public class Home extends Activity {

    private ImageView image;
    private Button logout,editProfile,sellBook,buyBook, viewHistory,viewCart,pendingOrders,myUploads;
    private User user;
    private TextView greeting;
    private String password;
    private Bundle bundle;
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.home);
        initViews();

        bundle= getIntent().getExtras();
        user = (User) getIntent().getSerializableExtra("user");
        password = bundle.getString("password");

        SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", user.getUser_email());
        editor.putString("password", password);
        editor.apply();

        ViewCompat.setBackgroundTintList(buyBook, null);
        ViewCompat.setBackgroundTintList(viewCart, null);
        ViewCompat.setBackgroundTintList(viewHistory, null);
        ViewCompat.setBackgroundTintList(sellBook, null);
        ViewCompat.setBackgroundTintList(editProfile, null);
        ViewCompat.setBackgroundTintList(logout, null);

        if(user!=null){
            String text="Hi "+user.getUser_first_name();
            greeting.setText(text);
        }

        buyBook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                displayBuyBook(user);
            }
        });

        sellBook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                displaySellBook(user);
            }
        });

        viewHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                displayViewHistory(user);
            }
        });

        viewCart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                displayMyCart(user);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                displayEditProfile(user);
            }
        });
        pendingOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPendingOrders(user);
            }
        });

        myUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayMyUploads(user);
            }
        });

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Logout();
            }
        });

    }
    @Override
    public void onActivityResult(int request,int result,Intent intent){

    }

    private void initViews(){
        buyBook= findViewById(R.id.buy_book);
        sellBook= findViewById(R.id.sell_book);
        viewCart= findViewById(R.id.view_cart);
        viewHistory= findViewById(R.id.view_history);
        editProfile= findViewById(R.id.edit_profile);
        logout=findViewById(R.id.logout);
        greeting= findViewById(R.id.greeting);
        pendingOrders = findViewById(R.id.pending_orders);
        myUploads = findViewById(R.id.my_uploads);

    }

    private void displayBuyBook(User user){
        Intent i = new Intent(Home.this, BookSearch.class);
        i.putExtra("user",user);
        startActivityForResult(i,1);
    }

    private void displaySellBook(User user){
        Intent i = new Intent(Home.this,SellBook.class);
        i.putExtra("user",user);
        startActivityForResult(i,2);
    }

    private void displayViewHistory(User user){
        Intent i = new Intent(Home.this,ViewHistory.class);
        i.putExtra("user",user);
        startActivityForResult(i,3);
    }

    private void displayEditProfile(User user){
        Intent i = new Intent(Home.this,EditProfile.class);
        i.putExtra("user",user);
        startActivityForResult(i,4);
    }

    private void displayMyCart(User user){
        Intent i = new Intent(Home.this,MyCart.class);
        i.putExtra("user",user);
        startActivityForResult(i,5);
    }


    public void displayPendingOrders(User user){
        Intent i = new Intent(Home.this,PendingOrders.class);
        i.putExtra("user",user);
        startActivityForResult(i,6);
    }

    public void displayMyUploads(User user){
        Intent i = new Intent(Home.this,MyUploads.class);
        i.putExtra("user",user);
        startActivityForResult(i,7);
    }

    private void Logout(){
        SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("email");
        editor.remove("password");
        editor.apply();

        Intent i = new Intent(Home.this, Login.class);
        Toast.makeText(Home.this,"Logged out",Toast.LENGTH_SHORT).show();
        startActivity(i);
        finish();
    }

}
