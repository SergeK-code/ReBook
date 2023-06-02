package com.example.rebook;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchResult extends Activity {
    private GridView book_grid;
    private CircleImageView cart;
    private BooksAdapter booksAdapter;
    private ResultBooksAPI booksResultAPI;
    private Book book;
    private int school_id,grade_id,category_id;
    private ArrayList<Book> resultBooks;
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.search_result);
        initViews();
        book = (Book) getIntent().getSerializableExtra("selectedBook");
        school_id=book.getSchool_id();
        grade_id=book.getGrade_id();
        category_id=book.getCategory_id();

        showResults();

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
}