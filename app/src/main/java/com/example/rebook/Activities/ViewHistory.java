package com.example.rebook.Activities;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;


import com.example.rebook.AsyncTasks.GetBooksAPI;
import com.example.rebook.AsyncTasks.GetOperationTypesAPI;
import com.example.rebook.AsyncTasks.GetOperationsAPI;
import com.example.rebook.Models.Book;
import com.example.rebook.Models.Operation;
import com.example.rebook.Models.Operation_type;
import com.example.rebook.Models.User;
import com.example.rebook.Adapters.OrderAdapter;
import com.example.rebook.R;

import java.util.ArrayList;

import java.util.concurrent.ExecutionException;


public class ViewHistory extends Activity {
    private ListView Operations_list;
    private OrderAdapter Operation_adapter;
    private ArrayList<Operation> operations = new ArrayList<>();
    private ArrayList<Operation> filteredOperations = new ArrayList<>();
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Operation_type> operationTypes = new ArrayList<>();
    private GetOperationsAPI getOperations;
    private GetBooksAPI getBooks;
    private GetOperationTypesAPI getOperationTypes;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_history);
        initViews();

        user = (User) getIntent().getSerializableExtra("user");

        listOfOperations();

    }
    public void initViews(){
        Operations_list = findViewById(R.id.orders);
    }

    public void listOfOperations(){
        getOperations = new GetOperationsAPI(ViewHistory.this);
        getBooks =new GetBooksAPI(ViewHistory.this);
        getOperationTypes = new GetOperationTypesAPI(ViewHistory.this);
        try {
            books = getBooks.execute().get();
            operationTypes = getOperationTypes.execute().get();
            operations = getOperations.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        for(Operation op : operations){
            if(op.getUser_id()==user.getUser_id() && (op.getOperation_type_id()==1 ||op.getOperation_type_id()==4 ) && op.getOperation_status_id()==3){
                filteredOperations.add(op);
            }
        }
        Operation_adapter = new OrderAdapter(ViewHistory.this,filteredOperations,books,operationTypes);

    }
}
