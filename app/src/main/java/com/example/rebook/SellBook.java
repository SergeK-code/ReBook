package com.example.rebook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SellBook extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageView;
    private Spinner bookSchool, bookGrade, bookCategory, bookName;
    private EditText conditionEditText;
    private Button uploadButton;
    private ArrayAdapter<School> schoolAdapter;
    private ArrayList<School> schools;
    private School selectedSchool;
    private int selectedSchoolId = 0;
    private GetSchoolsAPI dbSchools;
    private ArrayAdapter<Grade> gradeAdapter;
    private ArrayList<Grade> grades;
    private Grade selectedGrade;
    private int selectedGradeId = 0;
    private GetGradesAPI dbGrades;
    private ArrayAdapter<Category> categoryAdapter;
    private ArrayList<Category> categories;
    private Category selectedCategory;
    private int selectedCategoryId = 0;
    private GetCategoriesAPI dbCategories;
    private ArrayAdapter<Book> bookAdapter;
    private ArrayList<Book> books;
    private ArrayList<Book> selectedBooks;
    private Book selectedBook;
    private int selectedBookId;
    private GetBooksAPI dbBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_book);

        imageView = findViewById(R.id.imageView);
        bookSchool = findViewById(R.id.book_school);
        bookGrade = findViewById(R.id.book_grade);
        bookCategory = findViewById(R.id.book_category);
        bookName = findViewById(R.id.book_name);
        conditionEditText = findViewById(R.id.condition);
        uploadButton = findViewById(R.id.search);
        schools = new ArrayList<>();
        grades = new ArrayList<>();
        categories = new ArrayList<>();
        books = new ArrayList<>();
        selectedBooks = new ArrayList<>();

        schools = getSchools();
        listOfSchools();
        bookSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSchool = (School) parent.getSelectedItem();
                selectedSchoolId = selectedSchool.getSchool_id();

                listOfBooks(selectedSchoolId, selectedGradeId, selectedCategoryId);
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
                selectedGrade = (Grade) parent.getSelectedItem();
                selectedGradeId = selectedGrade.getGrade_id();

                listOfBooks(selectedSchoolId, selectedGradeId, selectedCategoryId);
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
                selectedCategory = (Category) parent.getSelectedItem();
                selectedCategoryId = selectedCategory.getCategory_id();

                listOfBooks(selectedSchoolId, selectedGradeId, selectedCategoryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        books = getBooks();
        selectedBooks = new ArrayList<>();
        bookAdapter = new ArrayAdapter<Book>(this, android.R.layout.simple_spinner_item, selectedBooks);
        bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookName.setAdapter(bookAdapter);
        bookName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBook = (Book) parent.getSelectedItem();
                selectedBookId = selectedBook.getBook_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnUpload = findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadBook();
            }
        });
    }

    ArrayList<School> getSchools() {
        ArrayList<School> results = new ArrayList<>();
        this.dbSchools = new GetSchoolsAPI(this);
        try {
            results = dbSchools.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    private void listOfSchools() {
        schoolAdapter = new ArrayAdapter<School>(this, android.R.layout.simple_spinner_item, schools);
        schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookSchool.setAdapter(schoolAdapter);
    }

    ArrayList<Grade> getGrades() {
        ArrayList<Grade> results = new ArrayList<>();
        this.dbGrades = new GetGradesAPI(this);
        try {
            results = dbGrades.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    private void listOfGrades() {
        gradeAdapter = new ArrayAdapter<Grade>(this, android.R.layout.simple_spinner_item, grades);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookGrade.setAdapter(gradeAdapter);
    }

    ArrayList<Category> getCategories() {
        ArrayList<Category> results = new ArrayList<>();
        this.dbCategories = new GetCategoriesAPI(this);
        try {
            results = dbCategories.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    private void listOfCategories() {
        categoryAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookCategory.setAdapter(categoryAdapter);
    }

    ArrayList<Book> getBooks() {
        ArrayList<Book> results = new ArrayList<>();
        this.dbBooks = new GetBooksAPI(this);
        try {
            results = dbBooks.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    private void listOfBooks(int selectedSchoolId, int selectedGradeId, int selectedCategoryId) {
        selectedBooks.clear();
        for (Book b : books) {
            if (b.getSchool_id() == selectedSchoolId && b.getGrade_id() == selectedGradeId && b.getCategory_id() == selectedCategoryId) {
                selectedBooks.add(b);
            }
        }
        bookAdapter.notifyDataSetChanged();
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadBook() {
        String school = bookSchool.getSelectedItem().toString();
        String grade = bookGrade.getSelectedItem().toString();
        String category = bookCategory.getSelectedItem().toString();
        String Name = bookName.getSelectedItem().toString();
        String condition = conditionEditText.getText().toString();

        if (school.isEmpty() || grade.isEmpty() || category.isEmpty() || Name.isEmpty() || condition.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = addBookToDatabase(school, grade, category, Name, condition);

        if (success) {
            Toast.makeText(this, "Book uploaded successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to upload book", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean addBookToDatabase(String school, String grade, String category, String Name, String condition) {
        SetBook setB = new SetBook(school,grade, category, Name, condition);
        String response = null;
        try {
            response= setB.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(this,response,Toast.LENGTH_SHORT).show();
        return true;
    }
}
