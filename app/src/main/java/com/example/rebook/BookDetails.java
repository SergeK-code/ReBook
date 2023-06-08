package com.example.rebook;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BookDetails extends Activity {
    private TextView category,title,school,grade,price;
    private ImageView book_image;
    private Button AddToCartBtn;
    private String school_name,category_name,grade_name;
    private Book selectedBook;
    private Bundle bundle;
    private GetOperationsAPI getOperations;
    private String result;
    private User user;
    private AddToCartAPI addToCart;
    private ArrayList<Operation> operations= new ArrayList<>();
    private ArrayList<Operation> myAddToCartOperations= new ArrayList<>();


    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.book_details);

        initViews();
        setViews();
        getMyAddToCartOperations();
        bundle = getIntent().getExtras();
        school_name = bundle.getString("selectedSchool");
        category_name = bundle.getString("selectedCategory");
        grade_name = bundle.getString("selectedGrade");
        selectedBook = (Book) getIntent().getSerializableExtra("selectedBook");
        user = (User) getIntent().getSerializableExtra("user");

        AddToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean found =false;
                for(Operation op : myAddToCartOperations){
                    if(op.getBook_id() == selectedBook.getBook_id()){
                        Toast.makeText(BookDetails.this,"Book already added",Toast.LENGTH_SHORT).show();
                        found = true;
                        break;
                    }
                }
               if(!found){
                   addToCart = new AddToCartAPI(BookDetails.this,user.getUser_id(),selectedBook.getBook_id(),selectedBook.getBook_price());
                   try {
                      result = addToCart.execute().get();
                   } catch (ExecutionException | InterruptedException e) {
                       e.printStackTrace();
                   }
                   Toast.makeText(BookDetails.this,result,Toast.LENGTH_SHORT).show();
                   Intent i = new Intent(BookDetails.this,MyCart.class);
                   startActivityForResult(i,1);
               }
            }
        });
    }
    public void initViews(){
        category = findViewById(R.id.book_category);
        title = findViewById(R.id.book_name);
        school = findViewById(R.id.book_school);
        grade = findViewById(R.id.book_grade);
        price = findViewById(R.id.book_price);
        book_image = findViewById(R.id.book_image);
        AddToCartBtn = findViewById(R.id.add_to_cart_btn);
    }

    public void setViews(){
      category.setText(category_name);
      school.setText(school_name);
      title.setText(selectedBook.getBook_name());
      grade.setText(grade_name);
      price.setText(selectedBook.getBook_price());

        String imagePath = selectedBook.getBook_image_path();
        Glide.with(BookDetails.this)
                .load(imagePath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(book_image);

    }

    public void getMyAddToCartOperations(){
        operations.clear();
        myAddToCartOperations.clear();

        getOperations = new GetOperationsAPI(BookDetails.this);
        try {
            operations = getOperations.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        for(Operation op : operations){
            if(op.getUser_id()==user.getUser_id() && op.getOperation_type_id()==2 && op.getOperation_status_id()==1){
                myAddToCartOperations.add(op);
            }
        }


    }
}