package com.example.rebook.Adapters;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rebook.IP;
import com.example.rebook.Models.Book;
import com.example.rebook.Models.Operation;
import com.example.rebook.Models.Operation_type;
import com.example.rebook.Models.User;
import com.example.rebook.R;

import java.util.ArrayList;

public class OrderAdapter extends ArrayAdapter<Operation> {
    private Context context;
    private ArrayList<Operation> Operations,AllOperations;
    private ArrayList<Book> Books;
    private ArrayList<Operation_type> OperationTypes;
    private ArrayList<User> Users;
    private static final String Repo = "http://"+IP.ip+"/API_Rebook/";


    public OrderAdapter( Context context, ArrayList<Operation> operations,ArrayList<Operation> allOperations, ArrayList<Book> books, ArrayList<Operation_type> operationTypes,ArrayList<User> users) {

        super(context,0,operations);
        this.context = context;
        this.Operations = operations;
        this.Books = books;
        this.OperationTypes = operationTypes;
        this.AllOperations = allOperations;
        this.Users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.operation_list_item, parent, false);
        }

        Operation currentOperation = Operations.get(position);

        ImageView bookIcon = view.findViewById(R.id.BookImageView);
        TextView operationIdTextView = view.findViewById(R.id.order_idTextView);
        TextView bookNameTextView = view.findViewById(R.id.book_nameTextView);
        TextView operationTypeTextView = view.findViewById(R.id.operation_typeTextView);
        TextView dateTextView = view.findViewById(R.id.date_textView);
        TextView NameTextView = view.findViewById(R.id.name_textView);

        operationIdTextView.setText(String.valueOf(currentOperation.getOperation_id()));


        dateTextView.setText(currentOperation.getOperation_date());
        String image_path = "";
        Book book = null;
        int curr_op_book_id = currentOperation.getBook_id();
        for(Book b : Books){
            if(b.getBook_id()==curr_op_book_id){

                book = b;
                break;
            }
        }
        assert book != null;
        bookNameTextView.setText(book.getBook_name());
        image_path = Repo+book.getBook_image_path();

        int curr_op_type_id = currentOperation.getOperation_type_id();
        for(Operation_type opType : OperationTypes){
            if(opType.getOperation_type_id() == curr_op_type_id){
                operationTypeTextView.setText(opType.getOperation_type_name());
                break;
            }
        }


        Glide.with(context)
                .load(image_path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(bookIcon);
        int userId = 0;
        String userName = "";
        if(currentOperation.getOperation_type_id()==1){
            for(Operation op: AllOperations){
                if(op.getOperation_status_id() == 3 && op.getOperation_type_id() == 4) {
                    assert book != null;
                    if (op.getBook_id()==book.getBook_id()) {
                        userId = op.getUser_id();
                        break;
                    }
                }
            }
        }
        else{
            for(Operation op: AllOperations){
                if(op.getOperation_status_id() == 3 && op.getOperation_type_id() == 1) {
                    assert book != null;
                    if (op.getBook_id()==book.getBook_id()) {
                        userId = op.getUser_id();
                        break;
                    }
                }
            }
        }
        for(User user : Users){
            if(user.getUser_id()==userId){
                NameTextView.setText(user.getUser_first_name()+" "+user.getUser_last_name());
                break;
            }
        }




        return view;
    }
}
