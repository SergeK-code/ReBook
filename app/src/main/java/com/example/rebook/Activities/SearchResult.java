package com.example.rebook.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.rebook.Adapters.BooksAdapter;
import com.example.rebook.AsyncTasks.GetBooksAPI;
import com.example.rebook.AsyncTasks.GetOperationsAPI;
import com.example.rebook.Models.Book;
import com.example.rebook.Models.Operation;
import com.example.rebook.Models.User;
import com.example.rebook.R;
import com.example.rebook.AsyncTasks.ResultBooksAPI;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchResult extends Activity {
    private GridView book_grid;
    private CircleImageView cart_icon;
    private TextView badge_counter;
    private BooksAdapter booksAdapter;
    private ResultBooksAPI booksResultAPI;
    private Book book,selectedBook;
    private String school,grade,category;
    private int school_id,grade_id,category_id;
    private User user;
    private Button Back,Cancel;
    private GetBooksAPI getBooks;
    private GetOperationsAPI getOperations;
    private ArrayList<Operation> operations = new ArrayList<>();
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Book> selectedBooks = new ArrayList<>();

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.search_result);

        initViews();
        Bundle bundle = getIntent().getExtras();
        book = (Book) getIntent().getSerializableExtra("selectedBook");
        school_id=book.getSchool_id();
        grade_id=book.getGrade_id();
        category_id=book.getCategory_id();

        school = bundle.getString("selectedSchool");
        grade = bundle.getString("selectedGrade");
        category = bundle.getString("selectedCategory");
        user = (User) getIntent().getSerializableExtra("user");

        showResults();
        book_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedBook = (Book) parent.getAdapter().getItem(position);
                displayBookDetails(selectedBook);
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2);
                finish();
            }
        });

        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchResult.this,MyCart.class);
                i.putExtra("user",user);
                startActivityForResult(i,2);
            }
        });

    }




    public void initViews(){
        book_grid = findViewById(R.id.book_grid);
        cart_icon = findViewById(R.id.cart_icon);
        Back = findViewById(R.id.back_btn);
        Cancel = findViewById(R.id.cancel_btn);

    }

    public void showResults(){

        ArrayList<Operation> filteredOperations = new ArrayList<>();
        ArrayList<Integer> bookIds = new ArrayList<>();
        ArrayList<Book> filteredBooks = new ArrayList<>();
        getOperations = new GetOperationsAPI(SearchResult.this);
        getBooks = new GetBooksAPI(SearchResult.this);
        try {
            operations.clear();
            books.clear();
            operations = getOperations.execute().get();
            books = getBooks.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        for(Operation op : operations){
            if(op.getOperation_type_id()==1 && op.getOperation_status_id()==1 && op.getUser_id()!=user.getUser_id()){
                filteredOperations.add(op);
                bookIds.add(op.getBook_id());
            }
        }
        selectedBooks.clear();
        for(Book b : books){
            for(int bookId : bookIds){
                if(b.getBook_id()==bookId){
                    filteredBooks.add(b);
                    break;
                }
            }
        }
        for(Book b : filteredBooks){
            if(b.getSchool_id()==school_id && b.getGrade_id()==grade_id && b.getCategory_id()==category_id){
                selectedBooks.add(b);
            }
        }
        booksAdapter = new BooksAdapter(this,selectedBooks);
        book_grid.setAdapter(booksAdapter);
    }
    public void displayBookDetails(Book b){
        Intent i = new Intent(SearchResult.this,BookDetails.class);
        i.putExtra("selectedBook",selectedBook);
        i.putExtra("selectedSchool",school);
        i.putExtra("selectedGrade",grade);
        i.putExtra("selectedCategory",category);
        i.putExtra("user",user);
        startActivityForResult(i,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 1:
                switch(resultCode){
                    case 1:
                        break;
                    case 2 :
                        setResult(2);
                        finish();
                        break;
                }
            case 2:
                switch(resultCode){
                    case 1:
                        break;
                    case 2 :
                        setResult(2);
                        finish();
                        break;
                }
        }
    }
}