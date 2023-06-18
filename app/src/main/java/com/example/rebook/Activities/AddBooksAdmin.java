package com.example.rebook.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rebook.AsyncTasks.AddBookAPI;
import com.example.rebook.AsyncTasks.GetBooksAPI;
import com.example.rebook.AsyncTasks.GetCartBooksAPI;
import com.example.rebook.AsyncTasks.GetCategoriesAPI;
import com.example.rebook.AsyncTasks.GetGradesAPI;
import com.example.rebook.AsyncTasks.GetOperationsAPI;
import com.example.rebook.AsyncTasks.GetSchoolsAPI;
import com.example.rebook.Models.Book;
import com.example.rebook.Models.Category;
import com.example.rebook.Models.Grade;
import com.example.rebook.Models.Operation;
import com.example.rebook.Models.User;
import com.example.rebook.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AddBooksAdmin extends AppCompatActivity {

    private Spinner bookGrade, bookCategory;
    private Button addButton, back, cancel;
    private ArrayAdapter<Grade> gradeAdapter;
    private ArrayList<Grade> grades = new ArrayList<>();
    private Grade selectedGrade;
    private int selectedGradeId = 0;
    private GetGradesAPI dbGrades;
    private ArrayAdapter<Category> categoryAdapter;
    private ArrayList<Category> categories;
    private Category selectedCategory;
    private int selectedCategoryId = 0;
    private EditText bookNameEditText,priceEditText,isbnEditText;
    private GetCategoriesAPI dbCategories;
    private ArrayAdapter<Book> bookAdapter;
    private ArrayList<Book> selectedBooks = new ArrayList<>();
    private Book selectedBook;
    private int selectedBookId, school_id;
    private GetBooksAPI dbBooks;
    private String result;
    private User user;
    String bookName = " ";
    int price = 0;
    String isbn = " ";
    private boolean added= false;
    private GetOperationsAPI getOperations;
    private ArrayList<Operation> allOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book);
        user = (User) getIntent().getSerializableExtra("user");

        bookGrade = findViewById(R.id.book_grade);
        bookCategory = findViewById(R.id.book_category);
        bookNameEditText = findViewById(R.id.book_name);
        back = findViewById(R.id.back_btn);
        cancel = findViewById(R.id.cancel_btn);
        addButton = findViewById(R.id.add);
        priceEditText = findViewById(R.id.book_price);
        isbnEditText = findViewById(R.id.isbn);
        grades = new ArrayList<>();
        categories = new ArrayList<>();

        grades = getGrades();
        listOfGrades();
        categories = getCategories();
        listOfCategories();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 bookName = bookNameEditText.getText().toString();
                 price = Integer.parseInt(priceEditText.getText().toString());
                 isbn = isbnEditText.getText().toString();
                ArrayList<Book> currentBooks = listOfBooks(selectedGradeId,selectedCategoryId);
                added = false;

                if(!currentBooks.isEmpty()){
                    added = true;
                }

                if (added) {
                    Toast.makeText(AddBooksAdmin.this, "This book already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    AddBookAPI addBook = new AddBookAPI(AddBooksAdmin.this,user.getSchool_id(),selectedGradeId,selectedCategoryId,bookName,price,isbn,user.getUser_id());
                    try {
                        result = addBook.execute().get();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(AddBooksAdmin.this,result,Toast.LENGTH_SHORT).show();
                }
            }
        });

        bookCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = (Category) parent.getAdapter().getItem(position);
                selectedCategoryId = selectedCategory.getCategory_id();

                listOfBooks(selectedGradeId, selectedCategoryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        bookGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGrade = (Grade) parent.getAdapter().getItem(position);
                selectedGradeId = selectedGrade.getGrade_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2);
                finish();
            }
        });
    }

    private ArrayList<Grade> getGrades() {
        ArrayList<Grade> results = new ArrayList<>();
        GetGradesAPI dbGrades = new GetGradesAPI(this);
        try {
            results = dbGrades.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    private void listOfGrades() {
        gradeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, grades);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookGrade.setAdapter(gradeAdapter);
    }

    private ArrayList<Category> getCategories() {
        ArrayList<Category> results = new ArrayList<>();
        GetCategoriesAPI dbCategories = new GetCategoriesAPI(this);

        try {
            results = dbCategories.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    private void listOfCategories() {
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookCategory.setAdapter(categoryAdapter);
    }

    private ArrayList<Book> getBooks() {
        ArrayList<Book> results = new ArrayList<>();
        GetBooksAPI dbBooks = new GetBooksAPI(this);

        try {
            results = dbBooks.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    private ArrayList<Book> listOfBooks(int selectedGradeId, int selectedCategoryId) {
        selectedBooks.clear();
        ArrayList<Book> filteredBooks = new ArrayList<>();
        ArrayList<Book> books = getBooks();
        for (Book b : books) {
            if (b.getGrade_id() == selectedGradeId && b.getCategory_id() == selectedCategoryId && b.getBook_name()==bookName) {
                filteredBooks.add(b);
            }
        }


        getOperations = new GetOperationsAPI(AddBooksAdmin.this);
        try {
            allOperations = getOperations.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<Operation> myOperations = new ArrayList<>();
        for(Operation op: allOperations){
            if(op.getUser_id()==user.getUser_id() && op.getOperation_type_id()==5 && op.getOperation_status_id()==1){
                myOperations.add(op);
            }
        }

        for(Book b: filteredBooks){
            for(Operation op : myOperations)
                if(b.getBook_id() == op.getBook_id()){
                    selectedBooks.add(b);
                            break;
                }
        }


       return selectedBooks;
    }
}
