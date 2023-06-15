package com.example.rebook.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.rebook.Adapters.BooksAdapter;
import com.example.rebook.Models.Book;
import com.example.rebook.Models.User;
import com.example.rebook.R;
import com.example.rebook.AsyncTasks.ResultBooksAPI;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchResult extends Activity {
    private GridView book_grid;
    private CircleImageView cart;
    private BooksAdapter booksAdapter;
    private ResultBooksAPI booksResultAPI;
    private Book book,selectedBook;
    private String school,grade,category;
    private int school_id,grade_id,category_id;
    private ArrayList<Book> resultBooks;
    private User user;

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

    }

    public void initViews(){
        book_grid = findViewById(R.id.book_grid);
        cart = findViewById(R.id.cart_icon);
    }

    public void showResults(){
        booksResultAPI = new ResultBooksAPI(this,school_id,grade_id,category_id);
        try {
            resultBooks=booksResultAPI.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        booksAdapter = new BooksAdapter(this,resultBooks);
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
}