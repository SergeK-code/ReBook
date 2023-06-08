package com.example.rebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BookSearch extends Activity {

    private Spinner bookSchool,bookGrade,bookCategory,bookName;
    private Button search,back,cancel;

    private ArrayAdapter<School> school_adapter;
    private ArrayList<School> schools;
    private School selectedSchool;
    private int selectedSchoolId=0;
    private GetSchoolsAPI db_schools;

    private ArrayAdapter<Grade> grade_adapter;
    private ArrayList<Grade> grades;
    private Grade selectedGrade;
    private int selectedGradeId=0;
    private GetGradesAPI db_grades;

    private ArrayAdapter<Category> category_adapter;
    private ArrayList<Category> categories;
    private Category selectedCategory;
    private int selectedCategoryId=0;
    private GetCategoriesAPI db_categories;

    private ArrayAdapter<Book> book_adapter;
    private ArrayList<Book> books;
    private ArrayList<Book> selectedBooks;
    private Book selectedBook;
    private int selectedBookId;
    private GetBooksAPI db_books;

    private User user;


    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.book_search);
        initViews();
        schools = new ArrayList<>();
        grades = new ArrayList<>();
        categories = new ArrayList<>();
        books = new ArrayList<>();
        selectedBooks = new ArrayList<>();

        user = (User) getIntent().getSerializableExtra("user");


        schools = getSchools();
        listOfSchools();
        bookSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSchool = (School) parent.getAdapter().getItem(position);
                selectedSchoolId = selectedSchool.getSchool_id();

                listOfBooks(selectedSchoolId,selectedGradeId,selectedCategoryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        grades = getGrades();
        listOfGrades();
        bookGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGrade = (Grade) parent.getAdapter().getItem(position);
                selectedGradeId = selectedGrade.getGrade_id();

                listOfBooks(selectedSchoolId,selectedGradeId,selectedCategoryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        categories = getCategories();
        listOfCategories();
        bookCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = (Category) parent.getAdapter().getItem(position);
                selectedCategoryId = selectedCategory.getCategory_id();

                listOfBooks(selectedSchoolId,selectedGradeId,selectedCategoryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        books = getBooks();
        book_adapter = new ArrayAdapter<Book>(this, android.R.layout.simple_spinner_item,selectedBooks);
        book_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookName.setAdapter(book_adapter);
        bookName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBook = (Book) parent.getAdapter().getItem(position);
                selectedBookId = selectedBook.getBook_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display_SearchResult(selectedBook);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    void initViews(){
        bookSchool = findViewById(R.id.book_school);
        bookGrade = findViewById(R.id.book_grade);
        bookCategory = findViewById(R.id.book_category);
        bookName = findViewById(R.id.book_name);
        search = findViewById(R.id.search);
        back = findViewById(R.id.back_btn);
        cancel = findViewById(R.id.cancel_btn);
    }

    ArrayList<School> getSchools(){
        ArrayList<School> results = new ArrayList<>();
        this.db_schools = new GetSchoolsAPI(this);
        try {
            results = db_schools.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    private void listOfSchools(){
        school_adapter = new ArrayAdapter<School>(this, android.R.layout.simple_spinner_item,schools);
        school_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookSchool.setAdapter(school_adapter);
    }

    ArrayList<Grade> getGrades(){
        ArrayList<Grade> results = new ArrayList<>();
        this.db_grades = new GetGradesAPI(this);
        try {
            results = db_grades.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return results;

    }

    private void listOfGrades(){
        grade_adapter = new ArrayAdapter<Grade>(this, android.R.layout.simple_spinner_item,grades);
        grade_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookGrade.setAdapter(grade_adapter);
    }

    ArrayList<Category> getCategories(){
        ArrayList<Category> results = new ArrayList<>();
        this.db_categories = new GetCategoriesAPI(this);
        try {
            results = db_categories.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return results;

    }

    private void listOfCategories(){
        category_adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item,categories);
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookCategory.setAdapter(category_adapter);
    }

    ArrayList<Book> getBooks(){
        ArrayList<Book> results = new ArrayList<>();
        this.db_books = new GetBooksAPI(this);
        try {
            results = db_books.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return results;

    }

    private void listOfBooks(int selectedSchoolId,int selectedGradeId,int selectedCategoryId){
        selectedBooks.clear();
        for(Book b : books){
            if(b.getSchool_id()==selectedSchoolId && b.getGrade_id()==selectedGradeId && b.getCategory_id()==selectedCategoryId){
                selectedBooks.add(b);
            }
        }
        book_adapter.notifyDataSetChanged();
    }

    void display_SearchResult(Book selectedBook){
        Intent i = new Intent(BookSearch.this,SearchResult.class);
        i.putExtra("selectedBook",selectedBook);
        i.putExtra("selectedSchool",selectedSchool.getSchool_name());
        i.putExtra("selectedGrade",selectedGrade.getGrade_name());
        i.putExtra("selectedCategory",selectedCategory.getCategory_name());

        i.putExtra("user",user);


        startActivityForResult(i,1);

    }


}
