package com.example.rebook.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.view.menu.MenuWrapperICS;

import com.example.rebook.Adapters.BuyOperationAdapter;
import com.example.rebook.AsyncTasks.GetBooksAPI;
import com.example.rebook.AsyncTasks.GetOperationsAPI;
import com.example.rebook.AsyncTasks.GetUsersAPI;
import com.example.rebook.Models.Book;
import com.example.rebook.Models.Operation;
import com.example.rebook.Models.User;
import com.example.rebook.R;
import com.example.rebook.Adapters.SellOperationAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class PendingOrders extends Activity {

    private static final ArrayList<String> options = new ArrayList<>(Arrays.asList("Buy","Sell"));
    private ArrayList<Operation> Operations = new ArrayList<>();

    private ArrayList<Operation> ResultOperations = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Book> Books = new ArrayList<>();

    private BuyOperationAdapter buyOperationAdapter;
    private SellOperationAdapter sellOperationAdapter;
    private GetOperationsAPI getOperations;
    private GetBooksAPI getBooks;
    private GetUsersAPI getUsers;
    private ListView myOperations;
    private Button back;
    private Spinner orderType;
    private ArrayAdapter<String> optionsAdapter;
    private User user;


    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.request);
        initViews();
        user = (User) getIntent().getSerializableExtra("user");

        optionsAdapter = new ArrayAdapter<>(PendingOrders.this, android.R.layout.simple_spinner_item,options);
        optionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        orderType.setAdapter(optionsAdapter);

        orderType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int selected = adapterView.getSelectedItemPosition();
                if(selected == 0){
                    getBuyOperations();
                }
                else{
                    getSellOperations();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void initViews(){
        myOperations = findViewById(R.id.my_orders);
        back = findViewById(R.id.back_btn);
        orderType = findViewById(R.id.orderType);
    }

    public void getBuyOperations(){
        ResultOperations.clear();
        getOperations = new GetOperationsAPI(PendingOrders.this);
        getBooks = new GetBooksAPI(PendingOrders.this);
        try {
            Operations = getOperations.execute().get();
            Books = getBooks.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        for(Operation op : Operations){
            if(op.getOperation_type_id()==4 && op.getOperation_status_id()==2 && op.getUser_id()==user.getUser_id()){
                ResultOperations.add(op);
            }
        }
        buyOperationAdapter = new BuyOperationAdapter(PendingOrders.this,ResultOperations,Books);
        myOperations.setAdapter(buyOperationAdapter);
    }

    public void getSellOperations(){
        ArrayList<Operation> SellOperations = new ArrayList<>();
        ArrayList<Operation> BuyOperations=new ArrayList<>();
        ResultOperations.clear();
        getOperations = new GetOperationsAPI(PendingOrders.this);
        getBooks = new GetBooksAPI(PendingOrders.this);
        getUsers = new GetUsersAPI(PendingOrders.this);
       try {
            Operations = getOperations.execute().get();
            Books= getBooks.execute().get();
            users = getUsers.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        for(Operation op : Operations){
            if(op.getOperation_type_id()==1 && op.getOperation_status_id()==2 && op.getUser_id()==user.getUser_id()){
                ResultOperations.add(op);
            }
        }
        for(Operation op : Operations){
            if(op.getOperation_type_id()==4 && op.getOperation_status_id()==2){
                BuyOperations.add(op);
            }
        }
        sellOperationAdapter = new SellOperationAdapter(PendingOrders.this,ResultOperations,BuyOperations,Books,users);
        myOperations.setAdapter(sellOperationAdapter);
    }

}
