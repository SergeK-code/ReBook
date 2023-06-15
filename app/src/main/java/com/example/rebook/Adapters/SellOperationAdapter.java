package com.example.rebook.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.rebook.Models.Book;
import com.example.rebook.Models.Operation;
import com.example.rebook.Models.User;
import com.example.rebook.R;

import java.util.ArrayList;

public class SellOperationAdapter extends ArrayAdapter<Operation> {
    private ArrayList<Operation> operations;
    private ArrayList<Book> books;
    private ArrayList<User> users;

    public SellOperationAdapter(Context context, ArrayList<Operation> operations, ArrayList<Book> books, ArrayList<User> users) {
        super(context, 0, operations);
        this.operations = operations;
        this.books = books;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Operation operation = operations.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.request_list_item_seller, parent, false);
        }

        TextView bookNameTextView = convertView.findViewById(R.id.cat_classTextView);
        TextView buyerNameTextView = convertView.findViewById(R.id.buyer_nameTextView);
        Button acceptButton = convertView.findViewById(R.id.accept);
        Button rejectButton = convertView.findViewById(R.id.reject);


        Book book = findBookById(operation.getBook_id());

        if (book != null) {
            bookNameTextView.setText(book.getBook_name());
        }
        else{bookNameTextView.setText("");}

        // Retrieve buyer details for the operation
        User buyer = findBuyerById(operation.getUser_id());

        if (buyer != null) {
            buyerNameTextView.setText(buyer.getUser_first_name());
        }
        else{buyerNameTextView.setText("");}

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    private Book findBookById(int bookId) {
        for (Book book : books) {
            if (book.getBook_id() == bookId) {
                return book;
            }
        }
        return null;
    }

    private User findBuyerById(int buyerId) {
        for (User user : users) {
            if (user.getUser_id() == buyerId) {
                return user;
            }
        }
        return null;
    }
}
