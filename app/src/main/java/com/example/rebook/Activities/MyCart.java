package com.example.rebook.Activities;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rebook.Adapters.BookCartAdapter;
import com.example.rebook.AsyncTasks.CheckoutItemsAPI;
import com.example.rebook.AsyncTasks.GetCartBooksAPI;
import com.example.rebook.AsyncTasks.GetCategoriesAPI;
import com.example.rebook.AsyncTasks.GetGradesAPI;
import com.example.rebook.AsyncTasks.GetSchoolsAPI;
import com.example.rebook.Models.Book;
import com.example.rebook.Models.Category;
import com.example.rebook.Models.Grade;
import com.example.rebook.Models.School;
import com.example.rebook.Models.User;
import com.example.rebook.R;
import com.example.rebook.AsyncTasks.RemoveFromCartAPI;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MyCart extends Activity {
    private TextView item_count;
    private GridView booksGrid;
    private Button checkout,clearCart;
    private GetCartBooksAPI getCartBooks;
    private ArrayList<Book> books = new ArrayList<>();
    private BookCartAdapter bookCartAdapter;
    private Book selectedBook;
    private int selectedBookId,school_id,grade_id,category_id;
    private GetSchoolsAPI getSchool;
    private GetGradesAPI getGrade;
    private GetCategoriesAPI getCategory;
    private RemoveFromCartAPI removeFromCart;
    private ArrayList<School> schools = new ArrayList<>();
    private ArrayList<Grade> grades = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();
    private String school_name,category_name,grade_name,result;
    private User user;
    private CheckoutItemsAPI checkoutItems;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.my_cart);

        initViews();
        user = (User) getIntent().getSerializableExtra("user");

        listOfBooks();
        booksGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedBook = (Book) parent.getAdapter().getItem(position);
                selectedBookId = selectedBook.getBook_id();
                school_id = selectedBook.getSchool_id();
                grade_id = selectedBook.getGrade_id();
                category_id = selectedBook.getCategory_id();
                displayBookDetails();
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutItems();
            }
        });

        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCartItems();
            }
        });

    }
    public void initViews(){
        item_count = findViewById(R.id.cart_items_count);
        booksGrid = findViewById(R.id.book_grid);
        checkout = findViewById(R.id.check_out_all);
        clearCart = findViewById(R.id.clear_cart);
    }

    public void listOfBooks(){
        getCartBooks = new GetCartBooksAPI(MyCart.this);
        try {
            books = getCartBooks.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        bookCartAdapter = new BookCartAdapter(MyCart.this,user.getUser_id(),books);
        booksGrid.setAdapter(bookCartAdapter);
    }

    public void displayBookDetails(){
        getSchool= new GetSchoolsAPI(MyCart.this);
        getGrade= new GetGradesAPI(MyCart.this);
        getCategory = new GetCategoriesAPI(MyCart.this);

        try {
            schools = getSchool.execute().get();
            grades = getGrade.execute().get();
            categories = getCategory.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        for(School s : schools){
            if(s.getSchool_id() == school_id){
                school_name = s.getSchool_name();
                break;
            }
        }for(Category c: categories){
            if(c.getCategory_id() == category_id){
                category_name = c.getCategory_name();
                break;
            }
        }for(Grade g : grades){
            if(g.getGrade_id() == grade_id){
               grade_name = g.getGrade_name();
                break;
            }
        }

        Intent i = new Intent(MyCart.this,BookDetails.class);
        i.putExtra("selectedBook",selectedBook);
        i.putExtra("selectedSchool",school_name);
        i.putExtra("selectedGrade",grade_name);
        i.putExtra("selectedCategory",category_name);
        i.putExtra("user",user);
        startActivityForResult(i,1);

    }

    public void checkoutItems(){
        checkoutItems = new CheckoutItemsAPI(MyCart.this,books,user.getUser_id());
        try {
            result = checkoutItems.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(MyCart.this,result, Toast.LENGTH_SHORT).show();

        books.clear();
        getCartBooks = new GetCartBooksAPI(MyCart.this);
        try {
            books = getCartBooks.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        bookCartAdapter.notifyDataSetChanged();
    }

    public void clearCartItems(){
       removeFromCart = new RemoveFromCartAPI(MyCart.this,user.getUser_id(),books);
        try {
            result = checkoutItems.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(MyCart.this,result, Toast.LENGTH_SHORT).show();

        if(result.toLowerCase().contains("successfully")){
            books.clear();
            bookCartAdapter.notifyDataSetChanged();
        }
    }
}
