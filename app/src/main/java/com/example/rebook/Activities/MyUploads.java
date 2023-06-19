package com.example.rebook.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Property;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rebook.Adapters.RemoveBookAdapter;
import com.example.rebook.AsyncTasks.GetBooksAPI;
import com.example.rebook.AsyncTasks.GetCartBooksAPI;
import com.example.rebook.AsyncTasks.GetCategoriesAPI;
import com.example.rebook.AsyncTasks.GetGradesAPI;
import com.example.rebook.AsyncTasks.GetOperationsAPI;
import com.example.rebook.Models.Book;
import com.example.rebook.Models.Category;
import com.example.rebook.Models.Grade;
import com.example.rebook.Models.Operation;
import com.example.rebook.Models.User;
import com.example.rebook.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MyUploads extends Activity {
    private RemoveBookAdapter removeBookAdapter;
    private ListView booksList;
    private User user;
    private int school_id;
    private ArrayList<Book> myBooks= new ArrayList<>();
    private Button home;
    private TextView noBooks;
    private ArrayList<Book> selectedBooks = new ArrayList<>();
    private ArrayList<Book> filteredBooks = new ArrayList<>();
    private ArrayList<Book> books = new ArrayList<>();
    private GetOperationsAPI getOperations;
    private ArrayList<Operation> allOperations = new ArrayList<>();
    private ArrayList<Operation> myOperations = new ArrayList<>();
    private ArrayList<Integer> myBooksIds= new ArrayList<>();
    private ArrayList<Grade> allGrades = new ArrayList<Grade>();
    private ArrayList<Category> allCategories = new ArrayList<>();
    private GetCategoriesAPI getCategories;
    private GetGradesAPI getGrades;
    private Button cancel,back;
    private GetBooksAPI getBooks;



    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.my_uploads);

        booksList= findViewById(R.id.listView);
        noBooks= findViewById(R.id.noBooks);
        cancel = findViewById(R.id.cancel_btn);
        back = findViewById(R.id.back_btn);

        user= (User) getIntent().getSerializableExtra("user");
        school_id = user.getSchool_id();

        myBooks();
        listOfBooks();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void myBooks() {
        getOperations = new GetOperationsAPI(MyUploads.this);
        try {
            allOperations = getOperations.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        myOperations.clear();
        for(Operation op: allOperations){
            if(op.getUser_id()==user.getUser_id() && op.getOperation_type_id()==1 && op.getOperation_status_id()==1){
                myOperations.add(op);
                myBooksIds.add(op.getBook_id());
            }
        }

        myBooks.clear();
        getBooks = new GetBooksAPI(MyUploads.this);
        try {
            books = getBooks.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        for(Book b : books){
            for(int myB :myBooksIds){
                if(b.getBook_id()==myB){
                    myBooks.add(b);
                    break;
                }
            }
        }

    }

    public void listOfBooks(){
        getCategories = new GetCategoriesAPI(MyUploads.this);
        getGrades = new GetGradesAPI(MyUploads.this);
        try {
            allCategories = getCategories.execute().get();
            allGrades = getGrades.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        removeBookAdapter = new RemoveBookAdapter(MyUploads.this,myBooks,allGrades,allCategories);
        booksList.setAdapter(removeBookAdapter);
    }
}

