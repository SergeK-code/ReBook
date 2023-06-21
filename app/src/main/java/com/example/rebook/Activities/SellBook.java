package com.example.rebook.Activities;

import com.example.rebook.AsyncTasks.GetBooksAPI;
import com.example.rebook.AsyncTasks.GetCategoriesAPI;
import com.example.rebook.AsyncTasks.GetGradesAPI;
import com.example.rebook.AsyncTasks.GetOperationsAPI;
import com.example.rebook.AsyncTasks.GetSchoolsAPI;
import com.example.rebook.AsyncTasks.SetBook;
import com.example.rebook.IP;
import com.example.rebook.Models.Book;
import com.example.rebook.Models.Category;
import com.example.rebook.Models.Grade;
import com.example.rebook.Models.Operation;
import com.example.rebook.Models.School;
import com.example.rebook.Models.User;
import com.example.rebook.R;

import java.io.IOException;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.Manifest;

public class SellBook extends Activity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 2;

    private ImageView imageView;
    private Button uploadImageBtn,cancel,back;
    private EditText conditionEditText;
    private Button uploadBtn;
    private String imgPath;
    private Spinner bookSchool,bookCategory,bookGrade,bookName;
    private Book selectedBook;
    private Category selectedCategory;
    private Grade selectedGrade;
    private School selectedSchool;
    private int selectedSchoolId,selectedBookId=-1,selectedGradeId,selectedCategoryId;
    private GetSchoolsAPI getSchoolsApi;
    private GetOperationsAPI getOperationsApi;
    private GetCategoriesAPI getCategoriesApi;
    private GetBooksAPI getBooksApi;
    private GetGradesAPI getGradesAPi;
    private Bitmap selectedImage;
    private ArrayList<School> schools;
    private ArrayList<Grade> grades;
    private ArrayList<Category> categories;
    private ArrayList<Book> books,selectedBooks,loadBooks,sameIdBooks;

    private User user;
    private ArrayAdapter<Book> book_adapter;
    private ArrayAdapter<School> school_adapter;
    private ArrayAdapter<Grade> grade_adapter;
    private ArrayAdapter<Category> category_adapter;

    private ArrayList<Operation> operations = new ArrayList<>(),loadOperations;

    private String SetBookResponse;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_book);

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


        book_adapter = new ArrayAdapter<Book>(this, android.R.layout.simple_spinner_item,selectedBooks);
        book_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookName.setAdapter(book_adapter);
        bookName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBook = (Book) parent.getAdapter().getItem(position);

                Log.e("#bn",selectedBook.getBook_name());

                selectedBookId = selectedBook.getBook_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Set click listener for selecting an image
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    openImageGallery();
            }
        });

        // Set click listener for uploading the book
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadBook();
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

    public void initViews(){
        imageView = findViewById(R.id.imageView);
        uploadImageBtn = findViewById(R.id.uploadImage);
        conditionEditText = findViewById(R.id.condition);
        uploadBtn = findViewById(R.id.upload);
        bookCategory = findViewById(R.id.bookCategory);
        bookName = findViewById(R.id.bookName);
        bookGrade = findViewById(R.id.bookGrade);
        bookSchool = findViewById(R.id.bookSchool);
        cancel = findViewById(R.id.cancel_btn);
        back = findViewById(R.id.back_btn);
    }


    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the image gallery
                openImageGallery();
            } else {
                // Permission denied
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }

        }
    }

    // Open the image gallery to select an image
    private void openImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the result of the image selection from the gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                // Get the bitmap from the URI
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                // Check the image's EXIF metadata for orientation
                ExifInterface exif = new ExifInterface(getContentResolver().openInputStream(uri));
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                // Rotate the bitmap based on the orientation
                bitmap = rotateBitmap(bitmap, orientation);
                selectedImage = bitmap;
                // Display the rotated bitmap
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }

        try {
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (rotatedBitmap != bitmap) {
                bitmap.recycle();
            }
            return rotatedBitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


    private void uploadBook() {
        // Get the book details from the form
        String condition = conditionEditText.getText().toString().trim();

        // Check if an image has been selected
        if ( selectedImage == null ||selectedBookId==-1 || condition.trim().isEmpty()) {
            Toast.makeText(this, "Please fill entire form", Toast.LENGTH_SHORT).show();
            return;
        }

        GetBooksAPI getBooks = new GetBooksAPI(SellBook.this);
        GetOperationsAPI getOperationsAPI = new GetOperationsAPI(SellBook.this);
        loadBooks = new ArrayList<>();
        sameIdBooks = new ArrayList<>();
        loadBooks = new ArrayList<>();
        ArrayList<Operation> filteredOperations = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();
        try {
            loadBooks = getBooks.execute().get();
            loadOperations = getOperationsAPI.execute().get();
        } catch (ExecutionException | InterruptedException e) {

            e.printStackTrace();
            return null;
        }

        for(Operation op : loadOperations){
            if(op.getOperation_type_id()==1 && (op.getOperation_status_id()==1 || op.getOperation_status_id()==2) && op.getUser_id()==user.getUser_id()){
                ids.add(op.getBook_id());
            }
        }
        ArrayList<Book> myUploadedBooks = new ArrayList<>();
        for(Book b : loadBooks){
            for(int id : ids){
                if(b.getBook_id()==id) {
                    myUploadedBooks.add(b);
                }
            }
        }
        for(Book b : myUploadedBooks){
            if(b.getBook_isbn()==selectedBook.getBook_isbn()){
                sameIdBooks.add(b);
            }
        }


        if(sameIdBooks.isEmpty()){
            // Convert the selected image to bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();

            // Create an OkHttp client
            OkHttpClient client = new OkHttpClient();

            // Create the request body with the image bytes
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", "book_image.jpg", RequestBody.create(MediaType.parse("image/jpeg"), imageBytes))
                    .addFormDataPart("condition", condition)
                    .build();

            String apiUrl = "http://" + IP.ip + "/API_Rebook/uploadImage.php";
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(requestBody)
                    .build();

            // Execute the request asynchronously
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SellBook.this, "Upload failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String responseData = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Handle the response from the server
                                //Toast.makeText(SellBook.this, responseData, Toast.LENGTH_SHORT).show();
                                imgPath = responseData;

                                SetBook setBook = new SetBook(SellBook.this,selectedSchoolId,selectedGradeId, selectedCategoryId, selectedBook.getBook_name(),condition, imgPath, selectedBook.getBook_price(),selectedBook.getBook_isbn(), user.getUser_id());
                                try {
                                    SetBookResponse = setBook.execute().get();

                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(SellBook.this,SetBookResponse,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
        else{
            Toast.makeText(SellBook.this,"Book already added",Toast.LENGTH_SHORT).show();
            return;
        }


    }

    ArrayList<School> getSchools(){
        ArrayList<School> results = new ArrayList<>();
        this.getSchoolsApi = new GetSchoolsAPI(this);
        try {
            results = getSchoolsApi.execute().get();
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
        this.getGradesAPi = new GetGradesAPI(this);
        try {
            results = getGradesAPi.execute().get();
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
        this.getCategoriesApi = new GetCategoriesAPI(this);
        try {
            results = getCategoriesApi.execute().get();
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
        this.getBooksApi = new GetBooksAPI(this);
        try {
            results = getBooksApi.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return results;

    }

    private void listOfBooks(int selectedSchoolId,int selectedGradeId,int selectedCategoryId){
        ArrayList<Operation> filteredOperations = new ArrayList<>();
        ArrayList<Integer> bookIds = new ArrayList<>();
        ArrayList<Book> filteredBooks = new ArrayList<>();
        getOperationsApi = new GetOperationsAPI(SellBook.this);
        try {
            operations.clear();
            operations = getOperationsApi.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        for(Operation op : operations){
            if(op.getOperation_type_id()==5 && op.getOperation_status_id()==1){
                filteredOperations.add(op);
                bookIds.add(op.getBook_id());
            }
        }
        selectedBooks.clear();
        books = getBooks();
        for(Book b : books){
            for(int bookId : bookIds){
                if(b.getBook_id()==bookId){
                    filteredBooks.add(b);
                    break;
                }
            }
        }
        for(Book b : filteredBooks){
            if(b.getSchool_id()==selectedSchoolId && b.getGrade_id()==selectedGradeId && b.getCategory_id()==selectedCategoryId){
                selectedBooks.add(b);
            }
        }

        if(!(selectedBooks.isEmpty())){
            selectedBook= selectedBooks.get(0);
        }

        book_adapter.notifyDataSetChanged();
    }
}

