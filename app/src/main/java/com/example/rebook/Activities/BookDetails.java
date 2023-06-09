package com.example.rebook.Activities;

import android.app.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rebook.AsyncTasks.AddToCartAPI;
import com.example.rebook.AsyncTasks.BuyBookAPI;
import com.example.rebook.AsyncTasks.GetOperationsAPI;
import com.example.rebook.AsyncTasks.GetPaymentMethodsAPI;
import com.example.rebook.AsyncTasks.GetUsersAPI;
import com.example.rebook.IP;
import com.example.rebook.Models.Book;
import com.example.rebook.Models.Operation;
import com.example.rebook.Models.User;
import com.example.rebook.Models.Payment_method;
import com.example.rebook.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BookDetails extends Activity {
    private TextView category,title,school,grade,price,phone,condition;
    private Spinner paymentMethod;
    private ImageView book_image;
    private Button AddToCartBtn,BuyBtn,back,cancel;
    private String school_name,category_name,grade_name;
    private Book selectedBook;
    private Bundle bundle;
    private GetOperationsAPI getOperations;
    private String result;
    private User user;
    private AddToCartAPI addToCart;
    private ArrayList<Operation> operations= new ArrayList<>();
    private ArrayList<Operation> myAddToCartOperations= new ArrayList<>();
    private ArrayList<Operation> myBuyOperations = new ArrayList<>();
    private GetUsersAPI getUsers;
    private ArrayList<Operation> allOperations = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private GetPaymentMethodsAPI getPaymentMethods;
    private ArrayList<Payment_method> payment_methods= new ArrayList<>();
    private ArrayAdapter<Payment_method> payment_methods_Adapter;
    private int selected_payment_method_id;
    private Payment_method selected_payment_method;
    private BuyBookAPI buyBookAPI;
    private static final String Repo = "http://"+ IP.ip+"/API_Rebook/";
    private static final int REQUEST_CODE_WHATSAPP = 1;
    private String phoneNumber;
    private PackageManager packageManager;


    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.book_details);

        bundle = getIntent().getExtras();
        school_name = bundle.getString("selectedSchool");
        category_name = bundle.getString("selectedCategory");
        grade_name = bundle.getString("selectedGrade");
        selectedBook = (Book) getIntent().getSerializableExtra("selectedBook");
        user = (User) getIntent().getSerializableExtra("user");

        getMyAddToCartOperations();
        initViews();
        setViews();

        listOfPaymentMethods();
        paymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_payment_method = (Payment_method) parent.getAdapter().getItem(position);
                selected_payment_method_id = selected_payment_method.getPayment_method_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 phoneNumber = phone.getText().toString();

                 packageManager = getPackageManager();
                String text = "Hi, I am interested in your book!";
                String encodedText = Uri.encode(text);
                String url = "https://api.whatsapp.com/send?phone="+phoneNumber+ "&text=" + encodedText;
                Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
                whatsappIntent.setData(Uri.parse(url));

                    startActivityForResult(whatsappIntent,REQUEST_CODE_WHATSAPP);

            }
        });



        AddToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyAddToCartOperations();
                boolean found =false;
                for(Operation op : myAddToCartOperations){
                    if(op.getBook_id() == selectedBook.getBook_id()){
                        Toast.makeText(BookDetails.this,"Book already added",Toast.LENGTH_SHORT).show();
                        found = true;
                        break;
                    }
                }
               if(!found){
                   addToCart = new AddToCartAPI(BookDetails.this,user.getUser_id(),selectedBook.getBook_id(),selectedBook.getBook_price(),selected_payment_method_id);
                   try {
                      result = addToCart.execute().get();
                   } catch (ExecutionException | InterruptedException e) {
                       e.printStackTrace();
                   }
                   Toast.makeText(BookDetails.this,result,Toast.LENGTH_SHORT).show();

               }
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

        BuyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyBuyOperations();
                boolean found =false;
                for(Operation op : myBuyOperations){
                    if(op.getBook_id() == selectedBook.getBook_id()){
                        Toast.makeText(BookDetails.this,"Book already bought by you",Toast.LENGTH_SHORT).show();
                        found = true;
                        break;
                    }
                }
                if(!found){
                    buyBookAPI = new BuyBookAPI(BookDetails.this,user.getUser_id(),selectedBook.getBook_id(),selectedBook.getBook_price(),selected_payment_method_id);
                    try {
                        result = buyBookAPI.execute().get();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e("#buy",result);
                    Toast.makeText(BookDetails.this,result,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WHATSAPP) {
            if (resultCode == RESULT_OK) {

            } else {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + phoneNumber));

                if (dialIntent.resolveActivity(packageManager) != null) {
                    startActivity(dialIntent);
                } else {
                    Toast.makeText(BookDetails.this, "No app found to handle the action", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void initViews(){
        category = findViewById(R.id.book_category);
        title = findViewById(R.id.book_name);
        school = findViewById(R.id.book_school);
        grade = findViewById(R.id.book_grade);
        price = findViewById(R.id.book_price);
        book_image = findViewById(R.id.book_image);
        AddToCartBtn = findViewById(R.id.add_to_cart_btn);
        BuyBtn = findViewById(R.id.buy_btn);
        phone = findViewById(R.id.phone);
        paymentMethod = findViewById(R.id.paymentMethod);
        back = findViewById(R.id.back_btn);
        cancel = findViewById(R.id.cancel_btn);
        condition = findViewById(R.id.book_condition);
    }

    public void setViews(){
      category.setText(category_name);
      school.setText(school_name);
      title.setText(selectedBook.getBook_name());
      grade.setText(grade_name);
      String priceText = String.valueOf(selectedBook.getBook_price() ) ;
      price.setText(priceText+" $");
      condition.setText(selectedBook.getBook_condition());

        String imagePath = Repo+selectedBook.getBook_image_path();
        Glide.with(BookDetails.this)
                .load(imagePath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(book_image);
      getUsers = new GetUsersAPI(BookDetails.this);
      getOperations = new GetOperationsAPI(BookDetails.this);
        try {
            users = getUsers.execute().get();
            allOperations = getOperations.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        int user_id = 0;
        String user_phone =" ";
        for(Operation op : allOperations){
            if(op.getBook_id() == selectedBook.getBook_id() && op.getOperation_type_id()==1){
                user_id = op.getUser_id();
                break;
            }
        }

        for(User u : users){
            if(u.getUser_id() == user_id){
                user_phone = u.getUser_phone();
                break;
            }
        }
        phone.setText(user_phone);

    }

    public void listOfPaymentMethods(){
        getPaymentMethods = new GetPaymentMethodsAPI(BookDetails.this);
        try {
            payment_methods = getPaymentMethods.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        payment_methods.remove(0);
        payment_methods_Adapter = new ArrayAdapter<Payment_method>(this, android.R.layout.simple_spinner_item,payment_methods);
        payment_methods_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethod.setAdapter(payment_methods_Adapter);
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
    public void getMyBuyOperations(){
        operations.clear();
        myBuyOperations.clear();

        getOperations = new GetOperationsAPI(BookDetails.this);
        try {
            operations = getOperations.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        for(Operation op : operations){
            if(op.getUser_id()==user.getUser_id() && op.getOperation_type_id()==4 && (op.getOperation_status_id()==1 || op.getOperation_status_id()==2)){
                myBuyOperations.add(op);
            }
        }

    }
}